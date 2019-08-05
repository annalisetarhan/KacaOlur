package com.annalisetarhan.kacaolur.waiting

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WaitingViewModel(application: Application): AndroidViewModel(application) {
    private val repository: CourierMessageRepository
    val allMessages: LiveData<List<CourierMessage>>
    val timeRemaining = MutableLiveData<String>()

    init {
        val messagesDao = CourierMessageRoomDatabase.getDatabase(application, viewModelScope).courierMessageDao()
        repository = CourierMessageRepository(messagesDao)
        allMessages = repository.allMessages
        timeRemaining.value = "00:00"
    }

    fun sendMessage(message: String) = viewModelScope.launch(Dispatchers.IO) {
        val timeStamp = Time().getTimestampString(getApplication())
        val courierMessage = CourierMessage(0, false, timeStamp, message)
        repository.sendMessage(courierMessage)
    }

    fun setUpCountdownTimer(context: Context) {
        val secondsRemaining = calculateCountdownTime(context)
        timeRemaining.value = Time(secondsRemaining).getStringForCountdown(context)
        startCountdown(secondsRemaining, context)
    }

    private fun calculateCountdownTime(context: Context): Int {
        val sharedPrefs = context.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)

        val deliveryTime = Time(sharedPrefs.getInt("delivery_time_in_seconds", 0))
        val currentTime = Time()
        val timeBidAccepted = Time(sharedPrefs.getString("time_bid_accepted_string", "")!!)
        val timePaused = Time(sharedPrefs.getInt("time_paused_in_seconds", 0))

        val deliveryTimeInSeconds = deliveryTime.getSeconds()
        var currentTimeInSeconds = currentTime.getSeconds()

        if (currentTimeInSeconds < deliveryTimeInSeconds) {
            currentTimeInSeconds += 86400
        }

        val timeBidAcceptedInSeconds = timeBidAccepted.getSeconds()
        val timePausedInSeconds = timePaused.getSeconds()

        return timeBidAcceptedInSeconds + deliveryTimeInSeconds + timePausedInSeconds - currentTimeInSeconds
    }

    private fun startCountdown(seconds: Int, context: Context) {
        object : CountDownTimer(seconds*1000.toLong(), 1000) {

            override fun onTick(p0: Long) {
                val secondsRemaining = Time((p0/1000).toInt())
                timeRemaining.value = secondsRemaining.getStringForCountdown(context)
            }

            override fun onFinish() {
                timeRemaining.value = "00:00"
            }
        }.start()
    }
}