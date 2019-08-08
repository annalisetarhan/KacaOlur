package com.annalisetarhan.kacaolur.confirming

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.Time
import com.annalisetarhan.kacaolur.waiting.CourierMessage
import com.annalisetarhan.kacaolur.waiting.CourierMessageRepository
import com.annalisetarhan.kacaolur.waiting.CourierMessageRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfirmingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CourierMessageRepository
    var sharedPrefs: SharedPreferences

    init {
        val messagesDao = CourierMessageRoomDatabase.getDatabase(application, viewModelScope).courierMessageDao()
        repository = CourierMessageRepository(messagesDao)
        sharedPrefs = application.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)
    }

    fun itemRejected(context: Context, complaint: String) {
        updateSharedPrefs(context)
        addComplaintToMessageThread(context, complaint)
        // TODO: tell server about complaint
    }

    private fun updateSharedPrefs(context: Context) {
        val secondsPaused = Time().getSecondsPaused(context)

        val editor = sharedPrefs.edit()
        editor.putInt("time_paused_in_seconds", secondsPaused)
        editor.putString("last_time_paused", "")
        editor.putBoolean("waiting_for_item_inspection", false)
        editor.apply()
    }

    private fun addComplaintToMessageThread(context: Context, complaint: String) = viewModelScope.launch(Dispatchers.IO) {
        val timeStamp = Time().getTimestampString(context)
        val complaintFormatted = context.resources.getString(R.string.reason_for_rejection, complaint)
        val courierMessage = CourierMessage(0, false, timeStamp, complaintFormatted)
        repository.sendMessage(courierMessage)
    }
}