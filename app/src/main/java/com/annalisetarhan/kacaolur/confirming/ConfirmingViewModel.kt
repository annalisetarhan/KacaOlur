package com.annalisetarhan.kacaolur.confirming

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
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

class ConfirmingViewModel @Inject constructor(
    private val sharedPrefs: SharedPreferencesStorage,
    private val repository: CourierMessageRepository
) : ViewModel() {

    fun itemRejected(context: Context, complaint: String) {
        updateSharedPrefs(context)
        addComplaintToMessageThread(context, complaint)
    }

    private fun updateSharedPrefs(context: Context) {
        val secondsPaused = Time().getSecondsPaused(context)

        sharedPrefs.setInt("time_paused_in_seconds", secondsPaused)
        sharedPrefs.setString("last_time_paused", "")
        sharedPrefs.setBoolean("waiting_for_item_inspection", false)
    }

    private fun addComplaintToMessageThread(context: Context, complaint: String) = viewModelScope.launch(Dispatchers.IO) {
        val currentDateTime = DateTime()
        val bidAcceptedDateTime = DateTime(sharedPrefs.getString("bid_accepted_datetime")!!)
        val timeStamp = currentDateTime.getTimestampString(context)
        val complaintFormatted = context.resources.getString(R.string.reason_for_rejection, complaint)
        repository.sendMessage(
            CourierMessage(
                currentDateTime.getSplitSecondsSince(bidAcceptedDateTime),
                currentDateTime.getString(),
                timeStamp,
                false,
                complaintFormatted)
        )
        // TODO: tell repository to tell server that item was rejected
    }

    fun getFormattedItemPrice(context: Context): String {
        val itemPrice = sharedPrefs.getFloat("item_price")
        return context.getString(R.string.item_price_fiyat, itemPrice)
    }
}