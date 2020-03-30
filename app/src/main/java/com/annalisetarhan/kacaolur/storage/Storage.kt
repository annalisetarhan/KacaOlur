package com.annalisetarhan.kacaolur.storage

interface Storage {
    fun setString(key: String, value: String)
    fun setBoolean(key: String, value: Boolean)
    fun setInt(key: String, value: Int)
    fun setFloat(key: String, value: Float)

    fun getString(key: String): String?
    fun getBoolean(key: String): Boolean
    fun getInt(key: String): Int
    fun getFloat(key: String): Float

    fun nuke()
}

/*
username : String
user_id : String
phone_number : String
request_id : String
order_id : String
item_name : String
item_description : String
photo_uri : String
courier_name : String
order_placed_datetime : String
bid_accepted_datetime : String
last_time_paused : String

time_paused_in_seconds : Int
delivery_time_in_seconds : Int

delivery_price : Float
item_price : Float

phone_number_validated : Boolean
order_submitted : Boolean
has_accepted_bid : Boolean
waiting_for_item_inspection : Boolean
has_picture : Boolean
*/
