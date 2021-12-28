package com.example.e_commence.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commence.Home
import com.example.e_commence.databinding.CategoryItemBinding
import com.example.e_commence.loadImage
import com.example.e_commence.model.CategoryObj
import com.example.e_commence.utils.Constants

/**
 * Created by Patrick Adutwum on 28/12/2021.
 */
class CategoryAdapter(val home: Home, private val categoryItem: ArrayList<CategoryObj>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root){
        val img = binding.img
        val title = binding.title
        val desc = binding.desc
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categoryItem[position]
        holder.img.loadImage(Constants.categoryImageUrl+item.image)
        holder.desc.text = item.description
        holder.title.text = item.name

        holder.itemView.setOnClickListener {
            home.addFrag(home.itemsHome.categoryProduct, position)
        }
    }

    override fun getItemCount(): Int {
        return categoryItem.size
    }
}