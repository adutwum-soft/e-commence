package com.example.e_commence.utils

/**
 * Created by Patrick Adutwum on 27/12/2021.
 */
class Constants {
    companion object{
        private const val baseUrl = "https://grocery.pegmex.live"
        const val login = "$baseUrl/api/login"
        const val register = "$baseUrl/api/register"
        const val categories = "$baseUrl/api/categories"
        const val products = "$baseUrl/api/products"
        const val logout = "$baseUrl/api/logout"

        const val viewCart = "$products/cart/view"
        const val addCart = "$products/cart/add/"
        const val deleteCart = "$products/cart/remove/"
        const val search = "$products/search/" //products/search/ap

        const val checkout = "$products/cart/checkout"
        const val history = "$baseUrl/api/orders/history"

        const val productImageUrl = "$baseUrl/storage/products/"
        const val categoryImageUrl = "$baseUrl/storage/category/"
    }
}