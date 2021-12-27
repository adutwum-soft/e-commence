package com.example.e_commence

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_commence.databinding.HomeBinding
import com.example.e_commence.utils.SessionManager

class Home : AppCompatActivity() {
    private lateinit var binding: HomeBinding
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}