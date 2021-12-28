package com.example.e_commence.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commence.Home
import com.example.e_commence.MainActivity
import com.example.e_commence.R
import com.example.e_commence.adapter.ProductAdapter
import com.example.e_commence.databinding.CategoryProductBinding
import com.example.e_commence.utils.Services

class CategoryProduct : Fragment() {
    private lateinit var _binding: CategoryProductBinding
    private val binding get() = _binding
    var home: Home? = null

    var services: Services? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        services = MainActivity.services
        home = activity as Home?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = CategoryProductBinding.inflate(inflater, container, false)

        with(binding){
            home?.setSupportActionBar(toolBar)
            val actionBar = home?.supportActionBar
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setHomeAsUpIndicator(R.drawable.ic_back_ios_24)
            }
            toolBar.setNavigationOnClickListener{
                home?.navigate(home?.itemsHome!!)
            }
        }

        init()

        return binding.root
    }

    private fun init(){
        setViewForProducts()
    }

    private fun setViewForProducts(){
        with(binding){
            services?.category?.categoryObj!![home!!.index!!].product?.let {
                home?.supportActionBar?.title = services?.category?.categoryObj!![home!!.index!!].name
                val productsAdapter = ProductAdapter(requireContext(), it.productObj!!,
                    services!!)
                productRecy.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
                productRecy.adapter = productsAdapter
                productsAdapter?.notifyDataSetChanged()
            }
        }
    }
}