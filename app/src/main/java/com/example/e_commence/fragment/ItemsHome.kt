package com.example.e_commence.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commence.Home
import com.example.e_commence.MainActivity
import com.example.e_commence.R
import com.example.e_commence.databinding.ItemsHomeBinding
import com.example.e_commence.utils.Services
import com.example.e_commence.utils.SessionManager
import okhttp3.OkHttpClient

class ItemsHome : Fragment() {
    private lateinit var _binding: ItemsHomeBinding
    private val binding get() = _binding

    lateinit var sessionManager: SessionManager
    var services: Services? = null
    private val client = OkHttpClient()
    var name = ""
    var home: Home? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireActivity())
        services = MainActivity.services
        home = activity as Home?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ItemsHomeBinding.inflate(inflater, container, false)
        with(binding){

        }
        return binding.root
    }

    companion object {

    }
}