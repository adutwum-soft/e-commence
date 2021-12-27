package com.example.e_commence.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.e_commence.R
import com.example.e_commence.model.User

/**
 * Created by Patrick Adutwum on 27/12/2021.
 */
class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private var cart: SharedPreferences = context.getSharedPreferences(context.getString(R.string.cart), Context.MODE_PRIVATE)

    companion object{
        var userToken = "token"
        private val KEY_USER_FULL_NAME = "name"
        private val KEY_USER_EMAIL = "email"
        private val KEY_USER_ID = "id"
        private val KEY_PHONE = "phone"
        private val BADGE = "badge"
    }

    fun saveUserToken(token: String){
        val editor = prefs.edit()
        editor.putString(userToken,token)
        editor.apply()
    }

    fun saveBadge(number: Int){
        val editor = cart.edit()
        editor.putInt(BADGE,number)
        editor.apply()
    }

    fun clearBadge(){
        val editor = cart.edit()
        editor.clear()
        editor.apply()
    }

    fun saveUserData(userData: User){
        val editor = prefs.edit()
        editor.putString(KEY_USER_ID, userData.id.toString())
        editor.putString(KEY_USER_FULL_NAME, userData.name)
        editor.putString(KEY_USER_EMAIL, userData.email)
        editor.putString(KEY_PHONE, userData.phone)
        editor.apply()
    }

    fun getUserToken():String{
        return prefs.getString(userToken,null).toString()
    }

    fun getBadge(): Int{
        return prefs.getInt(BADGE, 0)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getString(KEY_USER_FULL_NAME, null) != null
    }

    fun userFullName():String{
        return prefs.getString(KEY_USER_FULL_NAME,null).toString()
    }

    fun userEmail():String{
        return prefs.getString(KEY_USER_EMAIL,null).toString()
    }

    fun logout(): Boolean {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
        clearBadge()
        return true
    }
}