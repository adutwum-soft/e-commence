package com.example.e_commence.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commence.Home
import com.example.e_commence.databinding.ProductItemBinding
import com.example.e_commence.loadImage
import com.example.e_commence.model.ProductObj
import com.example.e_commence.snack
import com.example.e_commence.utils.Constants
import com.example.e_commence.utils.Services

/**
 * Created by Patrick Adutwum on 28/12/2021.
 */
class ProductAdapter(val context: Context, private  val productItem: ArrayList<ProductObj>, val services: Services):
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root){
        val img = binding.img
        val name = binding.name
        val price = binding.price
        val addToCart = binding.cart
        val share = binding.shareBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productItem[position]

        with(holder){
            img.loadImage(Constants.productImageUrl+item.image)
            name.text = item.name
            price.text = "GHC ${item.price.toString()}"

            addToCart.setOnClickListener {
                services.addCart(item.id.toString()).doOnError { it.printStackTrace() }.subscribe{
                    if (it.equals("Added to cart")){
                        (context as Home).showBadge()
                        addToCart.snack(it)
                    }
                }
            }

            share.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        return productItem.size
    }
}