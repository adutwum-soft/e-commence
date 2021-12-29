package com.example.e_commence.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commence.*
import com.example.e_commence.adapter.CategoryAdapter
import com.example.e_commence.adapter.ProductAdapter
import com.example.e_commence.api.Network.Companion.getRequest
import com.example.e_commence.databinding.ItemsHomeBinding
import com.example.e_commence.model.Category
import com.example.e_commence.utils.Constants
import com.example.e_commence.utils.Services
import com.example.e_commence.utils.SessionManager
import okhttp3.OkHttpClient

class ItemsHome : Fragment() {
    private lateinit var _binding: ItemsHomeBinding
    private val binding get() = _binding

    private lateinit var sessionManager: SessionManager
    var services: Services? = null
    private val client = OkHttpClient()
    var name = ""
    var home: Home? = null
    var categoryProduct: CategoryProduct = CategoryProduct()

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
        init()

        with(binding){
            swipe.setOnRefreshListener {
                loadCategories()
                loadProducts()
                swipe.isRefreshing = false
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!newText.isNullOrEmpty()){
                        newText.let {
                            services?.searchProduct(it)
                                ?.doOnError { error -> error(error.printStackTrace()) }
                                ?.subscribe{mess ->
                                    activity?.runOnUiThread {
                                        if (mess == "success"){
                                            homeLy.visibility = View.GONE
                                            searchView()
                                            searchRcy.visibility = View.VISIBLE
                                        }else{
                                            activity?.showToast(mess)
                                        }
                                    }
                                }
                        }
                    }else{
                        homeLy.visibility = View.VISIBLE
                        searchRcy.visibility = View.GONE
                    }
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    homeLy.visibility = View.GONE
                    return false
                }
            })
        }
        return binding.root
    }

    private fun init(){
        with(binding){
            greetUser.text  = if (sessionManager.isRem()){
                "${greetings()}, ${first(sessionManager.userFullName())}"
            }else{
                "${greetings()}, ${first(services?.user?.name!!)}"
            }
        }

        loadCategories()
        loadProducts()
    }

    fun searchView(){
        with(binding){
            val productsAdapter = ProductAdapter(requireContext(), services?.search?.productObj!!,
                services!!
            )
            searchRcy.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            searchRcy.adapter = productsAdapter
            productsAdapter?.notifyDataSetChanged()
        }
    }

    private fun loadCategories(){
        if (services?.category != null){
            setViews()
        }else{
            getRequest(Constants.categories, services!!).subscribe{ it ->
                activity?.runOnUiThread {
//                println(jsonObject)
                    services?.category = Category.fromJson(it)
                    setViews()
                }
            }
        }
    }

    private fun loadProducts(){
        if (services?.products != null){
            setViewForProducts()
        }else{
            services?.products()
                ?.doOnError { println(it.message) }
                ?.subscribe{
                    if (it == "success"){
                        activity?.runOnUiThread {
                            setViewForProducts()
                        }
                    }
                }
        }
    }

    private fun setViews(){
        with(binding){
            val categoryAdapter = CategoryAdapter(home!!, services?.category?.categoryObj!!)
            categoryRecy.layoutManager = GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, false)
            categoryRecy.adapter = categoryAdapter
            categoryAdapter?.notifyDataSetChanged()
        }
    }

    private fun setViewForProducts(){
        with(binding){
            val productsAdapter = ProductAdapter(requireContext(), services?.products?.productObj!!,
                services!!
            )
            productRecy.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            productRecy.adapter = productsAdapter
            productsAdapter?.notifyDataSetChanged()
        }
    }

    companion object {

    }
}