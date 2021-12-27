package com.example.e_commence.utils

import com.example.e_commence.MainActivity
import com.example.e_commence.model.*
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

/**
 * Created by Patrick Adutwum on 27/12/2021.
 */

class Services {
    var user: User? = null
    var category: Category? = null
    var mainActivity: MainActivity? = null
    var products: Products? = null
    var productDetail: Products? = null
    var search: Products? = null
    var cart: Cart? = null
    var order: Order? = null
    var totalAmount = 0.0

    var client = OkHttpClient()

    constructor(_mainActivity: MainActivity){
        mainActivity = _mainActivity
    }

    fun products(): Observable<String> {
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer ${mainActivity?.sessionManager?.getUserToken()}")
            .url(Constants.products)
            .build()
        return Observable.create { emitter: ObservableEmitter<String> ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val error = "Unable to Connect to Server"
                    emitter.onNext(error)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    val result = JSONArray(data)
                    println(result)

                    products = Products.fromJson(result)
                    emitter.onNext("success")
                }
            })
        }
    }

    fun viewCart(): Observable<String>{
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer ${mainActivity?.sessionManager?.getUserToken()}")
            .url(Constants.viewCart)
            .build()
        return Observable.create { emitter: ObservableEmitter<String> ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val error = "Unable to Connect to Server"
                    emitter.onNext(error)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    val result = JSONObject(data)
                    println(result)

                    if (result.getJSONArray("cart").length() == 0){
                        emitter.onNext("empty cart")
                    }else{
                        cart = Cart.fromJson(result.getJSONArray("cart"))
                        emitter.onNext("success")
                    }
                }
            })
        }
    }

    fun viewHistory(): Observable<String>{
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer ${mainActivity?.sessionManager?.getUserToken()}")
            .url(Constants.history)
            .build()
        return Observable.create { emitter: ObservableEmitter<String> ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val error = "Unable to Connect to Server"
                    emitter.onNext(error)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    println(">>> HISTORY >>> : $data")
                    order = Order.fromJson(JSONArray(data))
                    emitter.onNext("success")
                }
            })
        }
    }

    fun searchProduct(searchData: String): Observable<String>{
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer ${mainActivity?.sessionManager?.getUserToken()}")
            .url(Constants.search+searchData)
            .build()

        return Observable.create { emitter: ObservableEmitter<String> ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val error = "Unable to Connect to Server"
                    emitter.onNext(error)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    val result = JSONArray(data)

                    if (result.length() > 0){
                        search = Products.fromJson(result)
                        emitter.onNext("success")
                    }else{
                        emitter.onNext("no result")
                    }
                }
            })
        }
    }

    fun checkout(): Observable<String>{
        val body = ""
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer ${mainActivity?.sessionManager?.getUserToken()}")
            .url(Constants.checkout)
            .post(body.toRequestBody())
            .build()

        return Observable.create { emitter: ObservableEmitter<String> ->
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    val result = JSONObject(data)

                    if (result.has("message")){
                        if (result.getString("message").equals("Order placed successful")){
                            order = Order.fromJson(result.getJSONArray("order"))
                            emitter.onNext("done")
                        }else{
                            emitter.onNext(result.getString("message"))
                        }
                    }
                }
            })
        }
    }

    fun addCart(id: String): Observable<String>{
        val body: RequestBody = FormBody.Builder()
            .add("quantity", "1")
            .build()

        val request = Request.Builder()
            .addHeader("Authorization", "Bearer ${mainActivity?.sessionManager?.getUserToken()}")
            .url(Constants.addCart+id)
            .post(body)
            .build()

        return Observable.create { emitter: ObservableEmitter<String> ->
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    val result = JSONObject(data)

                    if (result.has("message")){
                        if (result.getJSONArray("cart").length() == 0){
                            emitter.onNext("empty cart")
                        }else{
                            cart = Cart.fromJson(result.getJSONArray("cart"))
                            emitter.onNext(result.getString("message"))
                        }
                    }
                }
            })
        }
    }

    fun removeItemFromCart(id: String): Observable<String>{
        val body = ""
        val request = Request.Builder()
            .addHeader("Authorization", "Bearer ${mainActivity?.sessionManager?.getUserToken()}")
            .url(Constants.deleteCart+id)
            .delete(body.toRequestBody())
            .build()

        return Observable.create { emitter: ObservableEmitter<String> ->
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    val result = JSONObject(data)

                    if (result.has("message")){
                        cart = Cart.fromJson(result.getJSONArray("cart"))
                        emitter.onNext(result.getString("message"))
                    }
                }
            })
        }
    }
}