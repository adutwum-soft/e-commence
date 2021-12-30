package com.example.e_commence.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commence.Home
import com.example.e_commence.MainActivity
import com.example.e_commence.R
import com.example.e_commence.adapter.CartAdapter
import com.example.e_commence.databinding.CartBinding
import com.example.e_commence.snack
import com.example.e_commence.utils.Services
import com.example.e_commence.utils.SessionManager

class Cart : Fragment() {
    private lateinit var _biding: CartBinding
    private val binding get() = _biding

    var home: Home? = null
    var services: Services? = null
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireActivity())
        services = MainActivity.services
        home = context as Home?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _biding = CartBinding.inflate(inflater, container, false)

        with(binding){
            home?.setSupportActionBar(toolBar)
            home?.supportActionBar?.title = activity?.getString(R.string.cart)
        }
        init()

        with(binding){
            checkoutBtn.setOnClickListener {
                progress.visibility = View.VISIBLE
                services?.checkout()
                    ?.doOnError { error(it.printStackTrace()) }
                    ?.subscribe{
                        activity?.runOnUiThread {
                            if (it.equals("done")){
                                loadCart()
                                sessionManager.clearBadge()
                                home?.let {
                                    home?.binding?.bottomNav?.selectedItemId = R.id.history
                                    home?.navigate(home?.history!!)
                                }
                            }
                        }
                    }
            }
        }
        return binding.root
    }

    private fun init(){
        loadCart()

        var sum = 0.0
        services?.cart?.cartObj?.let {
            for (i in 0 until it.size){
                val amount = it[i].product_amount.toDouble()
                val qty = it[i].product_qty.toDouble()
                val price = amount * qty
                sum += price
            }
        }
        with(binding){
            subValue.text = sum.toString()
            totalValue.text = sum.toString()
        }
    }

    private fun loadCart(){
        binding.progress.visibility = View.VISIBLE
        services?.viewCart()
            ?.doOnError { println(it.message) }
            ?.subscribe{
                activity?.runOnUiThread{
                    if (it == "success"){
//                        println(it)
                        setCartView()
                        emptyCart()
                    }else if (it == "empty cart"){
                        services?.cart = null
                        emptyCart(true)
                    }
                }
            }
    }

    private fun emptyCart(empty: Boolean = false){
        with(binding){
            if (empty){
                cartLy.visibility = View.GONE
                emptyCartLy.visibility = View.VISIBLE
                checkoutBtn.visibility = View.GONE
                low.visibility = View.GONE
            }else{
                cartLy.visibility = View.VISIBLE
                checkoutBtn.visibility = View.VISIBLE
                low.visibility = View.VISIBLE
                emptyCartLy.visibility = View.GONE
            }
            if (progress.visibility == View.VISIBLE){
                progress.visibility = View.GONE
            }
        }
    }

    private fun setCartView(){
        with(binding){
            val cartAdapter = CartAdapter(this@Cart, services?.cart?.cartObj!!)
            cartRecy.layoutManager = LinearLayoutManager(context)
            cartRecy.adapter = cartAdapter
            cartAdapter?.notifyDataSetChanged()
        }
    }

    fun alert(id: String, delete: ImageView){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Smart App")
        builder.setMessage("Are sure you want to Delete?")
        builder.setCancelable(true)

        builder.setPositiveButton("Yes") { _, _ ->
            services?.removeItemFromCart(id)?.doOnError { error -> error.printStackTrace() }?.subscribe{ mess ->
                activity?.runOnUiThread {
                    if (mess == "Item removed"){
                        delete.snack(mess, home?.binding?.bottomNav!!)
                        setCartView()
                    }
                }
            }
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    companion object {

    }
}