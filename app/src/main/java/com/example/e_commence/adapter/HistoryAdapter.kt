package com.example.e_commence.adapter

import android.content.Context
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commence.R
import com.example.e_commence.databinding.HistoryItemBinding
import com.example.e_commence.model.OrderObj
import java.util.*

/**
 * Created by Patrick Adutwum on 29/12/2021.
 */
class HistoryAdapter(private val context: Context, private val historyItem: ArrayList<OrderObj>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    inner class HistoryViewHolder(private val binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root){
        val listNum = binding.listNumber
        val orderId = binding.orderId
        val status = binding.status
        val date = binding.date

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyItem[position]
        val status = item.status

        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val dateZ: Date = dateFormat.parse(item.created_at)
        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateStr: String = formatter.format(dateZ)
        println(dateStr)

        holder.listNum.text = "${position + 1}"
        holder.orderId.text = item.orderNumber
        holder.date.text = "$dateStr"

        holder.status.text =  when(status){
            "0" -> { "pending" }
            "1" -> { "processing" }
            "2" -> { "canceled" }
            else -> { "completed" }
        }

        val color: Int = when(status){
            "0" -> { R.color.gold }
            "1" -> {R.color.darkBlue}
            "2" -> {R.color.red }
            else -> {R.color.green}
        }

        holder.status.setTextColor(ContextCompat.getColor(context, color))
    }

    override fun getItemCount(): Int {
        return historyItem.size
    }
}