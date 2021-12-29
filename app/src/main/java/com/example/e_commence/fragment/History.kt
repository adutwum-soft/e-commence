package com.example.e_commence.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commence.MainActivity
import com.example.e_commence.adapter.HistoryAdapter
import com.example.e_commence.databinding.HistoryBinding
import com.example.e_commence.utils.Services
import com.example.e_commence.utils.SessionManager

class History : Fragment() {
    private var _binding: HistoryBinding? = null
    private val binding get() = _binding!!

    lateinit var sessionManager: SessionManager
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
        _binding = HistoryBinding.inflate(inflater, container, false)

        init()

        binding.pullToRef.setOnRefreshListener {
            history()
        }

        return binding.root
    }

    private fun init(){
        history()
    }

    private fun history(){
        binding.progress.visibility = View.VISIBLE
        services?.viewHistory()?.doOnError { error -> error(error.printStackTrace()) }?.subscribe{
            activity?.runOnUiThread{
                with(binding){
                    if (it == "success"){
                        binding.pullToRef.isRefreshing = false
                        if (services?.order?.orderObj?.size!! > 0){
                            emptyHistLy.visibility = View.GONE
                            hisRecy.visibility = View.VISIBLE
                            setViews()
                        }else{
                            emptyHistLy.visibility = View.VISIBLE
                            hisRecy.visibility = View.GONE
                        }
                        binding.progress.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setViews(){
        with(binding){
            val categoryAdapter = HistoryAdapter(requireActivity(), services?.order?.orderObj!!)
            hisRecy.layoutManager = GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false)
            hisRecy.adapter = categoryAdapter
            categoryAdapter?.notifyDataSetChanged()

        }
    }
}