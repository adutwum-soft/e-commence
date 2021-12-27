package com.example.e_commence.model

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Patrick Adutwum on 27/12/2021.
 */
class Category {
    var categoryObj: ArrayList<CategoryObj>? = null

    constructor(_categoryObj: ArrayList<CategoryObj>){
        categoryObj = _categoryObj
    }

    companion object{
        fun fromJson(json: JSONArray): Category{
            val obj = ArrayList<CategoryObj>()
            var category: Category? = null
            try {
                for (i in 0 until json.length()) {
                    val categoryObj: CategoryObj = CategoryObj.fromJson(json.getJSONObject(i))
                    obj.add(categoryObj)
                }
                category = Category(obj)
            }catch (e: JSONException){
                e.printStackTrace()
            }
            return category!!
        }
    }
}

class CategoryObj{
    var id: Int? = null
    var name: String = ""
    var description: String = ""
    var image: String = ""
    var product: Products? = null

    constructor(
        _id: Int,
        _name: String,
        _description: String,
        _image: String,
        _product: Products
    ){
        id = _id
        name = _name
        description = _description
        image = _image
        product = _product
    }

    companion object{
        fun fromJson(json: JSONObject): CategoryObj{
            var categoryObj: CategoryObj? = null
            try {
                categoryObj = CategoryObj(
                    json.getInt("id"),
                    json.getString("name"),
                    json.getString("description"),
                    json.getString("image"),
                    Products.fromJson(json.getJSONArray("product"))
                )
            }catch (e: JSONException){
                e.printStackTrace()
            }
            return categoryObj!!
        }
    }
}