package com.example.e_commence

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.e_commence.databinding.LoginBinding
import com.example.e_commence.model.User
import com.example.e_commence.utils.Constants
import com.example.e_commence.utils.Services
import com.example.e_commence.utils.SessionManager
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class Login : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    lateinit var sessionManager: SessionManager
    var services: Services? = null
    var rem: Boolean = false

    private val okHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        services = MainActivity.services

        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            loginBtn.setOnClickListener {
                login()
            }
            signUpBtn.setOnClickListener {
                startActivity(Intent(this@Login, Signup::class.java))
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                rem = isChecked
            }
        }
    }

    private fun login(){
        with(binding){
            if (phone.text.toString().isEmpty() && pass.text.toString().isEmpty()){
                showToast("Phone number and password is required")
            }else{
                performLoading(true)

                val body: RequestBody = FormBody.Builder()
                    .add("phone", phone.text.toString())
                    .add("password", pass.text.toString())
                    .build()

                val request: Request = Request.Builder()
                    .url(Constants.login)
                    .post(body)
                    .build()

                okHttpClient.newCall(request).enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        performLoading()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val data = response.body!!.string()
                        runOnUiThread {
                            try {
                                val json = JSONObject(data)
                                println("result: $json")
                                if (json.has("token")){
                                    services?.user = User.fromJson(json)

                                    if (rem){
                                        sessionManager.saveUserData(services?.user!!)
                                        sessionManager.saveUserToken(services?.user?.token!!)
                                    }
                                    performLoading()
                                    startActivity(Intent(this@Login, Home::class.java))
                                    finish()
                                }else{
                                    showToast(json.getString("message"))
                                    performLoading()
                                }

                            }catch (e: JSONException){
                                e.printStackTrace()
                                performLoading()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun performLoading(load: Boolean = false){
        with(binding){
            progress.visibility = when(load) {
                true -> { View.VISIBLE}
                else -> { View.INVISIBLE }
            }
        }
    }
}