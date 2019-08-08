package com.annalisetarhan.kacaolur.payment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.Time
import com.annalisetarhan.kacaolur.waiting.CourierMessage
import com.annalisetarhan.kacaolur.waiting.CourierMessageRepository
import com.annalisetarhan.kacaolur.waiting.CourierMessageRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CourierMessageRepository
    private val sharedPrefs: SharedPreferences
    var itemPrice: Float
    val deliveryPrice: Float
    var totalPrice: Float

    init {
        val context = getApplication<Application>().applicationContext
        sharedPrefs = context.getSharedPreferences((R.string.shared_prefs_filename).toString(), 0)
        itemPrice = sharedPrefs.getFloat("item_price", 0f)
        deliveryPrice = sharedPrefs.getFloat("delivery_price", 5.5f)
        totalPrice = itemPrice + deliveryPrice

        val messagesDao = CourierMessageRoomDatabase.getDatabase(application, viewModelScope).courierMessageDao()
        repository = CourierMessageRepository(messagesDao)
    }

    fun update() {
        itemPrice = sharedPrefs.getFloat("item_price", 0f)
        totalPrice = itemPrice + deliveryPrice
    }

    fun pay(context: Context) {
        recordPayment(context)
        updateMessageThread(context)
    }

    private fun recordPayment(context: Context) {
        val secondsPaused = Time().getSecondsPaused(context)
        val editor = sharedPrefs.edit()

        editor.putBoolean("payment_confirmed", true)
        editor.putInt("time_paused_in_seconds", secondsPaused)
        editor.putString("last_time_paused", "")
        editor.putBoolean("waiting_for_item_inspection", false)

        editor.apply()
    }

    private fun updateMessageThread(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val timeStamp = Time().getTimestampString(context)
        val paymentConfirmedString = context.resources.getString(R.string.payment_confirmed)
        val courierMessage = CourierMessage(0, false, timeStamp, paymentConfirmedString)
        repository.sendMessage(courierMessage)
    }
}