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
    lateinit var sharedPrefs: SharedPreferences

    init {
        val messagesDao = CourierMessageRoomDatabase.getDatabase(application, viewModelScope).courierMessageDao()
        repository = CourierMessageRepository(messagesDao)
    }

    fun itemAccepted(context: Context) {
        updateSharedPrefs(context)
    }

    fun itemRejected(context: Context, complaint: String) {
        updateSharedPrefs(context)
        addComplaintToMessageThread(context, complaint)
        // TODO: tell server about complaint
    }

    private fun updateSharedPrefs(context: Context) {
        sharedPrefs = context.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)

        val secondsPaused = getSecondsPaused()

        val editor = sharedPrefs.edit()
        editor.putInt("time_paused_in_seconds", secondsPaused)
        editor.putString("last_time_paused", "")
        editor.putBoolean("waiting_for_item_inspection", false)
        editor.apply()
    }

    private fun getSecondsPaused(): Int {
        val currentTime = Time()
        val lastTimePaused = sharedPrefs.getString("last_time_paused", "")!!

        val timePausedThisTime =
            if (lastTimePaused == "") {
                0
            } else {
                currentTime.secondsSince(Time(lastTimePaused))
            }

        val timePausedPreviously = sharedPrefs.getInt("time_paused_in_seconds", 0)
        return timePausedThisTime + timePausedPreviously
    }

    private fun addComplaintToMessageThread(context: Context, complaint: String) = viewModelScope.launch(Dispatchers.IO) {
        val timeStamp = Time().getTimestampString(context)
        val complaintFormatted = context.resources.getString(R.string.reason_for_rejection, complaint)
        val courierMessage = CourierMessage(0, false, timeStamp, complaintFormatted)
        repository.sendMessage(courierMessage)
    }
}