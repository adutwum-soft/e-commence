package com.example.e_commence.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commence.databinding.CartItemBinding
import com.example.e_commence.fragment.Cart
import com.example.e_commence.model.CartObj

/**
 * Created by Patrick Adutwum on 28/12/2021.
 */
class CartAdapter(val cart: Cart, private val cartItem: ArrayList<CartObj>): RecyclerView.Adapter<CartAdapter.CartViewHolder>()  {
    inner class CartViewHolder(binding: CartItemBinding): RecyclerView.ViewHolder(binding.root){
        val listNum = binding.num
        val name = binding.name
        val quantity = binding.quantity
        val price = binding.price
        val delete = binding.deleteBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItem[position]
        val amount = item.product_amount.toDouble()
        val qty = item.product_qty.toDouble()
        val price = amount * qty

        holder.listNum.text = "${position + 1}."
        holder.name.text = item.product_name
        holder.quantity.text = item.product_qty
        holder.price.text = "GHC$price"


        holder.delete.setOnClickListener {
            cart.alert(item.id.toString(), holder.delete)
        }

    }

    override fun getItemCount(): Int {
        return cartItem.size
    }
}