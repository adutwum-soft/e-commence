package com.example.e_commence.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.e_commence.Login
import com.example.e_commence.MainActivity
import com.example.e_commence.api.Network
import com.example.e_commence.databinding.SettingsBinding
import com.example.e_commence.showToast
import com.example.e_commence.utils.Constants
import com.example.e_commence.utils.Services
import com.example.e_commence.utils.SessionManager
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody


class Settings : Fragment() {
    private var _binding: SettingsBinding? = null
    private val binding get() = _binding!!

    lateinit var sessionManager: SessionManager
    private val client = OkHttpClient()
    var services: Services? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(requireActivity())
        services = MainActivity.services
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = SettingsBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init(){
        with(binding){
            name.text = if (sessionManager.isRem()){
                sessionManager.userFullName()
            }else{
                services?.user?.name
            }

            email.text = if (sessionManager.isRem()){
                sessionManager.userEmail()
            }else{
                services?.user?.email
            }

            logoutBtn.setOnClickListener {
                progress.visibility = View.VISIBLE
                Network.makeRequest(Constants.logout, "".toRequestBody(), client, services?.tokenCheck()!!).doOnError {
                    it.printStackTrace()
                }.subscribe{
                    activity?.runOnUiThread {
                        println("logout result: $it")
                        if (it.has("message")){
                            if (it.getString("message").equals("Logged Out")){
                                sessionManager.logout()
                                startActivity(Intent(requireActivity(), Login::class.java))
                                activity?.finish()
                            }else{
                                activity?.showToast(it.getString("message"))
                            }
                        }
                        progress.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}