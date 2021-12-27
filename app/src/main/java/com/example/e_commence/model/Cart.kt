package com.example.e_commence.model

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Patrick Adutwum on 27/12/2021.
 */
class Cart {
    var cartObj: ArrayList<CartObj>? = null

    constructor(_cartObj: ArrayList<CartObj>){
        cartObj = _cartObj
    }

    companion object{
        fun fromJson(json: JSONArray): Cart{
            val obj = ArrayList<CartObj>()
            var cart: Cart? = null
            try {
                for (i in 0 until json.length()) {
                    val cartObj: CartObj = CartObj.fromJson(json.getJSONObject(i))
                    obj.add(cartObj)
                }
                cart = Cart(obj)
            }catch (e: JSONException){
                e.printStackTrace()
            }

            return cart!!
        }
    }
}

class CartObj{
    var id: Int? = null
    var user_id: String = ""
    var product_id: String = ""
    var product_name: String = ""
    var product_qty: String = ""
    var product_amount: String = ""
    var status: String = ""

    constructor(
        _id: Int,
        _user_id: String,
        _product_id: String,
        _product_name: String,
        _product_qty: String,
        _product_amount: String,
        _status: String
    ){
        id = _id
        user_id = _user_id
        product_id = _product_id
        product_name = _product_name
        product_qty = _product_qty
        product_amount = _product_amount
        status = _status
    }

    companion object{
        fun fromJson(json: JSONObject): CartObj{
            var cartObj: CartObj? = null
            try {
                cartObj = CartObj(
                    json.getInt("id"),
                    json.getString("user_id"),
                    json.getString("product_id"),
                    json.getString("product_name"),
                    json.getString("product_qty"),
                    json.getString("product_amount"),
                    json.getString("status"),
                )
            }catch (e: JSONException){
                e.printStackTrace()
            }
            return cartObj!!
        }
    }
}