package com.example.e_commence

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.e_commence.databinding.HomeBinding
import com.example.e_commence.fragment.Cart
import com.example.e_commence.fragment.History
import com.example.e_commence.fragment.ItemsHome
import com.example.e_commence.fragment.Settings
import com.example.e_commence.utils.Services
import com.example.e_commence.utils.SessionManager

class Home : AppCompatActivity() {
    private lateinit var binding: HomeBinding
    lateinit var sessionManager: SessionManager
    var services: Services? = null
    var index: Int? = null
    var cartStats = false

    var itemsHome: ItemsHome = ItemsHome()
    var cart: Cart = Cart()
    var history: History = History()
    var settings: Settings = Settings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        services = MainActivity.services

        binding = HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        with(binding){
            bottomNav.setOnNavigationItemSelectedListener { menu ->
                when(menu.itemId){
                    R.id.home ->{
                        navigate(itemsHome)
                        true
                    }
                    R.id.cart ->{
                        navigate(cart)
                        true
                    }
                    R.id.history ->{
                        navigate(history)
                        true
                    }
                    R.id.settings ->{
                        navigate(settings)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun init(){
        loadCart()
        navigate(itemsHome)
    }

    private fun loadCart(){
        services?.viewCart()
            ?.doOnError { println(it.message) }
            ?.subscribe{
                runOnUiThread{
                    if (it == "empty cart"){
                        cartStats = false
                    }else if (it == "success"){
                        services?.cart?.cartObj?.let { groc ->
                            if (groc.size > sessionManager.getBadge()){
                                showBadge()
                            }else{
                                showBadge(false)
                            }
                        }
                        cartStats = true
                    }
                }
            }
    }

    fun navigate(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.homeFragment, fragment).commit()
    }

    fun addFrag(fragment: Fragment, pos: Int){
        index = pos
        if (fragment.isAdded){
            return
        }else{
            navigate(fragment)
        }
    }

    fun showBadge(show: Boolean = true){
        val num = services?.cart?.cartObj?.size!!
        var badge = binding.bottomNav.getOrCreateBadge(R.id.cart)
        if (show){
            badge.number = num
            badge.isVisible = true
        }else{
            if (badge != null){
                sessionManager.saveBadge(services?.cart?.cartObj?.size!!)
                badge.isVisible = false
                badge.clearNumber()
            }
        }
    }
}