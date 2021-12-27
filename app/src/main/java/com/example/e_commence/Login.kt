package com.example.e_commence

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_commence.databinding.LoginBinding
import com.example.e_commence.utils.SessionManager
import okhttp3.OkHttpClient

class Login : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    lateinit var sessionManager: SessionManager

    private val okHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            loginBtn.setOnClickListener {
                login()
            }
            signUpBtn.setOnClickListener {
                startActivity(Intent(this@Login, Signup::class.java))
            }
        }
    }

    private fun login(){

    }
}