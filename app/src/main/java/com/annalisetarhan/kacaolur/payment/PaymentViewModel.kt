package com.annalisetarhan.kacaolur.payment

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.storage.SharedPreferencesStorage
import com.annalisetarhan.kacaolur.utils.Time
import com.annalisetarhan.kacaolur.waiting.CourierMessageRepository
import com.annalisetarhan.kacaolur.utils.DateTime
import com.annalisetarhan.kacaolur.waiting.CourierMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PaymentViewModel @Inject constructor(
    private val repository: CourierMessageRepository,
    private val sharedPrefs: SharedPreferencesStorage
) : ViewModel() {

    var itemPrice: Float
    val deliveryPrice: Float
    var totalPrice: Float

    init {
        itemPrice = sharedPrefs.getFloat("item_price")
        deliveryPrice = sharedPrefs.getFloat("delivery_price")
        totalPrice = itemPrice + deliveryPrice
    }

    fun update() {
        itemPrice = sharedPrefs.getFloat("item_price")
        totalPrice = itemPrice + deliveryPrice
    }

    fun pay(context: Context) {
        recordPayment(context)
        updateMessageThread(context)
    }

    private fun recordPayment(context: Context) {
        val secondsPaused = Time().getSecondsPaused(context)

        sharedPrefs.setBoolean("payment_confirmed", true)
        sharedPrefs.setInt("time_paused_in_seconds", secondsPaused)
        sharedPrefs.setString("last_time_paused", "")
        sharedPrefs.setBoolean("waiting_for_item_inspection", false)
    }

    private fun updateMessageThread(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val currentDateTime = DateTime()
        val bidAcceptedDateTime = DateTime(sharedPrefs.getString("bid_accepted_datetime")!!)
        repository.sendMessage(
            CourierMessage(
                currentDateTime.getSplitSecondsSince(bidAcceptedDateTime),
                currentDateTime.getString(),
                currentDateTime.getTimestampString(context),
                false,
                context.resources.getString(R.string.payment_confirmed)
            )
        )
    }
}