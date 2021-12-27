package com.example.e_commence.model

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Patrick Adutwum on 27/12/2021.
 */
class Order {
    var orderObj: ArrayList<OrderObj>? = null

    constructor(_orderObj: ArrayList<OrderObj>){
        orderObj = _orderObj
    }

    companion object{
        fun fromJson(json: JSONArray): Order{
            val obj = ArrayList<OrderObj>()
            var order: Order? = null
            try {
                for (i in 0 until json.length()){
                    val orderObj: OrderObj = OrderObj.fromJson(json.getJSONObject(i))
                    obj.add(orderObj)
                }
                order = Order(obj)
            }catch (e: JSONException){
                e.printStackTrace()
            }
            return order!!
        }
    }
}

class Payment{
    var id: Int? = null
    var order_id: String = ""
    var amount: String = ""
    var reference: String = ""
    var status: String = ""
    var created_at: String = ""

    constructor(
        _id: Int,
        _order_id: String,
        _amount: String,
        _reference: String,
        _status: String,
        _created_at: String,
    ){
        id = _id
        order_id = _order_id
        amount = _amount
        reference = _reference
        status = _status
        created_at = _created_at
    }

    companion object{
        fun fromJson(json: JSONObject): Payment{
            var paymentObj: Payment? = null
            try {
                paymentObj = Payment(
                    json.getInt("id"),
                    json.getString("order_id"),
                    json.getString("amount"),
                    json.getString("reference"),
                    json.getString("status"),
                    json.getString("created_at"),
                )
            }catch (e: JSONException){
                e.printStackTrace()
            }
            return paymentObj!!
        }
    }
}

class OrderObj{
    var id: Int? = null
    var user_id: String = ""
    var orderNumber: String = ""
    var status: String = ""
    var created_at: String = ""
    var orderdetail: OrderDetail? = null
    var payment: Payment? = null

    constructor(
        _id: Int,
        _user_id: String,
        _orderNumber: String,
        _status: String,
        _created_at: String,
        _orderdetail: OrderDetail,
        _payment: Payment
    ){
        id = _id
        user_id = _user_id
        orderNumber = _orderNumber
        status = _status
        created_at = _created_at
        orderdetail = _orderdetail
        payment = _payment
    }

    companion object{
        fun fromJson(json: JSONObject): OrderObj{
            var orderObj: OrderObj? = null
            try {
                orderObj = OrderObj(
                    json.getInt("id"),
                    json.getString("user_id"),
                    json.getString("orderNumber"),
                    json.getString("status"),
                    json.getString("created_at"),
                    OrderDetail.fromJson(json.getJSONArray("orderdetail")),
                    Payment.fromJson(json.getJSONObject("payment"))
                )
            }catch (e: JSONException){
                e.printStackTrace()
            }
            return orderObj!!
        }
    }
}

class OrderDetail{
    var orderDetailObj: ArrayList<OrderDetailObj>? = null

    constructor()

    constructor(_orderDetailObj: ArrayList<OrderDetailObj>){
        orderDetailObj = _orderDetailObj
    }

    companion object{
        fun fromJson(json: JSONArray): OrderDetail{
            val obj = ArrayList<OrderDetailObj>()
            var orderDetail: OrderDetail? = null
            try {
                for (i in 0 until json.length()){
                    val orderDetailObj: OrderDetailObj = OrderDetailObj.fromJson(json.getJSONObject(i))
                    obj.add(orderDetailObj)
                }
                orderDetail = OrderDetail(obj)
            }catch (e: JSONException){
                e.printStackTrace()
            }
            return orderDetail!!
        }
    }
}

class OrderDetailObj{
    var id: Int? = null
    var order_id: String = ""
    var product_id: String = ""
    var qty: String = ""
    var price: String = ""
    var status: String = ""

    constructor(
        _id: Int,
        _order_id: String,
        _product_id: String,
        _qty: String,
        _price: String,
        _status: String,
    ){
        id = _id
        order_id = _order_id
        product_id = _product_id
        qty = _qty
        price = _price
        status = _status
    }

    companion object{
        fun fromJson(json: JSONObject): OrderDetailObj{
            var orderDetailObj: OrderDetailObj? = null
            try {
                orderDetailObj = OrderDetailObj(
                    json.getInt("id"),
                    json.getString("order_id"),
                    json.getString("product_id"),
                    json.getString("qty"),
                    json.getString("price"),
                    json.getString("status")
                )
            }catch (e: JSONException){
                e.printStackTrace()
            }
            return orderDetailObj!!
        }
    }
}