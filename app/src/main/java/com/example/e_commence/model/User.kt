package com.example.e_commence.model

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Patrick Adutwum on 27/12/2021.
 */
class User {
    var id: Int? = null
    var name: String = ""
    var email: String = ""
    var token: String = ""
    var phone: String = ""

    constructor(_id: Int, _name: String, _email: String, _token: String, _phone: String){
        id = _id
        name = _name
        email = _email
        token = _token
        phone = _phone
    }

    companion object{
        fun fromJson(json: JSONObject): User{
            var user: User? = null
            try {
                val data = json.getJSONObject("user")
                user = User(
                    data.getInt("id"),
                    data.optString("name"),
                    data.optString("email"),
                    json.optString("token"),
                    json.optString("phone")
                )
            }catch (e: JSONException){
                e.printStackTrace()
            }
            return user!!
        }
    }
}