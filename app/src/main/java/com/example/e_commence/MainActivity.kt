package com.example.e_commence

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commence.utils.SessionManager

class MainActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)

        if (applicationContext.isNetworkConnected()){
            if (sessionManager.isLoggedIn()) {
//                startActivity(Intent(this, Home::class.java))
//                finish()
            }else{
                startActivity(Intent(this, Login::class.java))
                finish()
            }
        }else{
            this.showToast("please check your internet")
        }
    }
}