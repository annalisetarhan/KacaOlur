package com.annalisetarhan.kacaolur.ordering

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.annalisetarhan.kacaolur.storage.SharedPreferencesStorage
import com.annalisetarhan.kacaolur.utils.DateTime
import javax.inject.Inject

class OrderViewModel @Inject constructor(
    private val sharedPrefs: SharedPreferencesStorage
): ViewModel() {

    var orderSubmitted: Boolean
    var hasPicture: Boolean

    init {
        orderSubmitted = sharedPrefs.getBoolean("order_submitted")
        hasPicture = sharedPrefs.getBoolean("has_picture")
    }

    fun acceptOrder(order: Order) {
        orderSubmitted = true
        setItemInfo(order)
    }

    private fun setItemInfo(order: Order) {
        order.itemName?.let { sharedPrefs.setString("item_name", it) }
        order.itemDescription?.let { sharedPrefs.setString("item_description", it) }
        sharedPrefs.setBoolean("order_submitted", true)
        sharedPrefs.setBoolean("has_accepted_bid", false)
        sharedPrefs.setString("order_placed_datetime", DateTime().getString())
    }

    fun savePhoto(uri: Uri) {
        sharedPrefs.setBoolean("has_picture", true)
        sharedPrefs.setString("photo_uri", uri.toString())
        hasPicture = true
        // TODO: send photo to server
    }

    fun getItemName(): String? {
        return sharedPrefs.getString("item_name")
    }

    fun getItemDescription(): String? {
        return sharedPrefs.getString("item_description")
    }

    fun getPhotoUri(): Uri {
        val uriString = sharedPrefs.getString("photo_uri")
        return Uri.parse(uriString)
    }

    fun nukeData() {
        sharedPrefs.nuke()      // FOR TESTING ONLY
    }
}



