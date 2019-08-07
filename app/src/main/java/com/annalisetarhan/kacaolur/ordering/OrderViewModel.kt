package com.annalisetarhan.kacaolur.ordering

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.annalisetarhan.kacaolur.R

class OrderViewModel(application: Application): AndroidViewModel(application) {
    var orderAccepted: Boolean = false

    fun acceptOrder(order: Order) {
        orderAccepted = true
        setItemInfo(order)
    }

    private fun setItemInfo(order: Order) {
        val context = getApplication<Application>().applicationContext
        val sharedPrefs = context.getSharedPreferences((R.string.shared_prefs_filename).toString(), 0)
        val editor = sharedPrefs.edit()
        editor.putString("item_name", order.itemName)
        editor.putString("item_description", order.itemDescription)
        editor.putBoolean("has_accepted_bid", false)
        editor.apply()
    }

    fun savePhoto(uri: Uri) {
        val context = getApplication<Application>().applicationContext
        val sharedPrefs = context.getSharedPreferences((R.string.shared_prefs_filename).toString(), 0)
        val editor = sharedPrefs.edit()
        editor.putString("photo_uri", uri.toString())
        editor.apply()
        // TODO: send photo to server
    }
}

