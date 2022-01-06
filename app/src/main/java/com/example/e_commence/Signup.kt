package com.example.e_commence

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commence.databinding.SignupBinding
import com.example.e_commence.model.User
import com.example.e_commence.utils.Constants
import com.example.e_commence.utils.Services
import com.example.e_commence.utils.SessionManager
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class Signup : AppCompatActivity() {
    private lateinit var binding: SignupBinding

    private val client = OkHttpClient()

    var services: Services? = null
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        services = MainActivity.services

        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            signInBtn.setOnClickListener {
                onBackPressed()
            }

            signUpBtn.setOnClickListener {
                if (email.text.toString().isEmpty() && pass.text.toString().isEmpty()
                    && fullName.text.toString().isEmpty() && conPass.text.toString().isEmpty()
                    && phone.text.toString().isEmpty()){
                    showToast("please all fields are required")
                }else{
                    progress.visibility = View.VISIBLE

                    val body: RequestBody = FormBody.Builder()
                        .add("email", email.text.toString())
                        .add("password", pass.text.toString())
                        .add("password_confirmation", conPass.text.toString())
                        .add("name", fullName.text.toString())
                        .add("phone", phone.text.toString())
                        .build()

                    val request: Request = Request.Builder()
                        .url(Constants.register)
                        .post(body)
                        .build()

                    client.newCall(request).enqueue(object: Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            e.printStackTrace()
                            progress.visibility = View.INVISIBLE
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val data = response.body!!.string()
                            runOnUiThread {
                                try {
                                    val json = JSONObject(data)
                                    println("result: $json")
                                    if (json.has("token")){
                                        services?.user = User.fromJson(json)

                                        sessionManager.saveUserData(services?.user!!)
                                        sessionManager.saveUserToken(services?.user?.token!!)
                                        progress.visibility = View.INVISIBLE
                                        startActivity(Intent(this@Signup, Home::class.java))
                                        finish()

                                    }else{
                                        showToast(json.getString("message"))
                                        progress.visibility = View.INVISIBLE
                                    }

                                }catch (e: JSONException){
                                    e.printStackTrace()
                                    progress.visibility = View.INVISIBLE
                                }
                            }
                        }
                    })
                }
            }

            phone.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(text: Editable?) {
                    val code = "233"
                    if (text!!.isNotEmpty() && text.length > 0){
                        val prefix = "$code${phone.text}"
                        print(">>> Phone >>> $prefix")
                    }

//                    if (phone.text.toString().isNotEmpty() && phone.text!!.toString().length > 1){
//
//                    }
                }
            })
        }
    }
}