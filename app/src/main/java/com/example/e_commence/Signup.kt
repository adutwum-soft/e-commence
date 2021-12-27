package com.example.e_commence

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_commence.databinding.SignupBinding

class Signup : AppCompatActivity() {
    private lateinit var binding: SignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            signInBtn.setOnClickListener {
                onBackPressed()
            }
        }
    }
}