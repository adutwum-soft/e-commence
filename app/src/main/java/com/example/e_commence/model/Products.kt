package com.example.e_commence.model

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by Patrick Adutwum on 27/12/2021.
 */
class Products {
    var productObj: ArrayList<ProductObj>? = null

    constructor(_productObj: ArrayList<ProductObj>){
        productObj = _productObj
    }

    companion object{
        fun fromJson(json: JSONArray): Products{
            val obj = ArrayList<ProductObj>()
            var products: Products? = null
            try {
                for (i in 0 until json.length()) {
                    val productsObj: ProductObj = ProductObj.fromJson(json.getJSONObject(i))
                    obj.add(productsObj)
                }
                products = Products(obj)
            }catch (e: JSONException){
                e.printStackTrace()
            }
            return products!!
        }
    }
}

class ProductObj{
    var id: Int? = null
    var name: String = ""
    var categoryId: String = ""
    var description: String = ""
    var price: Double? = null
    var image: String = ""

    constructor(
        _id: Int,
        _name: String,
        _categoryId: String,
        _description: String,
        _price: Double,
        _image: String
    ){
        id = _id
        name = _name
        categoryId = _categoryId
        description = _description
        price = _price
        image = _image
    }

    companion object{
        fun fromJson(json: JSONObject): ProductObj{
            var productsObj: ProductObj? = null
            try {
                productsObj = ProductObj(
                    json.getInt("id"),
                    json.getString("name"),
                    json.getString("category_id"),
                    json.getString("description"),
                    json.getDouble("price"),
                    json.getString("image")
                )
            }catch (e: JSONException){
                e.printStackTrace()
            }

            return productsObj!!
        }
    }
}