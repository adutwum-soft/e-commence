package com.example.e_commence

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * Created by Patrick Adutwum on 27/12/2021.
 */

fun ImageView.loadImage(url: String){
    Glide.with(this.context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(this)
}

fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    val snackBar = Snackbar.make(this, message, duration)
    snackBar.show()
}

fun Context.isNetworkConnected(): Boolean{
    val connnectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        val network = connnectivityManager.activeNetwork ?: return false
        val activeNetwork = connnectivityManager.getNetworkCapabilities(network) ?: return false
        return when{
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }else{
        val networkInfo = connnectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}

fun greetings(): String{
    var response = ""
    val c: Calendar = Calendar.getInstance()

    when (c.get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> {
            response = "Good Morning"
        }
        in 12..15 -> {
            response = "Good Afternoon"
        }
        in 16..20 -> {
            response = "Good Evening"
        }
        in 21..23 -> {
            response = "Good Night"
        }
    }

    return response
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, message, duration).show()
}

fun first(name: String): String{
    val data = name.split(" ").toTypedArray()
    return data[0]
}