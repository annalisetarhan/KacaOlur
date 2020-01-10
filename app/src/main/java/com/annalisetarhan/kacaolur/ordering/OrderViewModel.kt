package com.annalisetarhan.kacaolur.ordering

import android.app.Application
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.annalisetarhan.kacaolur.R

class OrderViewModel(application: Application): AndroidViewModel(application) {
    var orderSubmitted: Boolean
    var hasPicture: Boolean
    private val sharedPrefs: SharedPreferences

    init {
        val context = getApplication<Application>().applicationContext
        sharedPrefs = context.getSharedPreferences((R.string.shared_prefs_filename).toString(), 0)
        orderSubmitted = sharedPrefs.getBoolean("order_submitted", false)
        hasPicture = sharedPrefs.getBoolean("has_picture", false)
    }

    fun acceptOrder(order: Order) {
        orderSubmitted = true
        setItemInfo(order)
    }

    private fun setItemInfo(order: Order) {
        val editor = sharedPrefs.edit()
        editor.putString("item_name", order.itemName)
        editor.putString("item_description", order.itemDescription)
        editor.putBoolean("order_submitted", true)
        editor.putBoolean("has_accepted_bid", false)
        editor.apply()
    }

    fun savePhoto(uri: Uri) {
        val editor = sharedPrefs.edit()
        editor.putBoolean("has_picture", true)
        editor.putString("photo_uri", uri.toString())
        editor.apply()
        hasPicture = true
        // TODO: send photo to server
    }

    fun getItemName(): String? {
        return sharedPrefs.getString("item_name", "")
    }

    fun getItemDescription(): String? {
        return sharedPrefs.getString("item_description", "")
    }

    fun getPhotoUri(): Uri {
        val uriString = sharedPrefs.getString("photo_uri", "")
        return Uri.parse(uriString)
    }

    fun nukeData() {
        sharedPrefs.edit().clear().apply()      // FOR TESTING ONLY
    }
}



