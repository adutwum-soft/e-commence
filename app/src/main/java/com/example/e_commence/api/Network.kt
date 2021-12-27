package com.example.e_commence.api

import com.example.e_commence.R
import com.example.e_commence.utils.SessionManager
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Created by Patrick Adutwum on 27/12/2021.
 */
class Network {
    companion object{
        fun makeRequest(url: String, body: RequestBody, ok: OkHttpClient, token: String): Observable<JSONObject> {
            logRequest(url, body)

            val req: Request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token")
                .post(body)
                .build()

            return Observable.create { emitter: ObservableEmitter<JSONObject> ->
                ok.newCall(req).enqueue(object: Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        try {
                            val v = JSONObject()
                                .put("message", R.string.somethingError)
                            emitter.onNext(v)
                        } catch (ex: JSONException) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val data = response.body!!.string()
                        when{
                            response.isSuccessful ->{
                                val results = JSONObject(data)
                                emitter.onNext(results)
                                logResponse(results)
                            }
                        }
                    }
                })
            }
        }

        fun getRequest(url: String, ok: OkHttpClient, sessionManager: SessionManager): Observable<JSONArray> {
            logRequest(url)
            val request = Request.Builder()
                .addHeader("Authorization", "Bearer ${sessionManager.getUserToken()}")
                .url(url)
                .build()
            return Observable.create { emitter: ObservableEmitter<JSONArray> ->
                ok.newCall(request).enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val data = response.body!!.string()
                        response.use {
                            if (response.isSuccessful){
                                val results = JSONArray(data)
                                emitter.onNext(results)
                                logResponse(results)
                            }
                        }
                    }
                })
            }
        }

        private fun logRequest(url: String, request: RequestBody) {
            println("Api: >>> INFO Request URL : $url")
            println("Api: >>> INFO Request BODY : $request")
        }
        private fun logRequest(url: String) {
            println("Api: >>> INFO Request URL : $url")
        }

        private fun logResponse(response: JSONObject) {
            println("Api: >>> INFO Response $response")
        }
        private fun logResponse(response: JSONArray) {
            println("Api: >>> INFO Response $response")
        }
    }
}