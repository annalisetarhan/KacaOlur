package com.annalisetarhan.kacaolur.waiting

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.annalisetarhan.kacaolur.storage.SharedPreferencesStorage
import com.annalisetarhan.kacaolur.utils.Time
import com.annalisetarhan.kacaolur.utils.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class WaitingViewModel @Inject constructor(
    private val repository: CourierMessageRepository,
    private val sharedPrefs: SharedPreferencesStorage
) : ViewModel() {

    val allMessages: LiveData<List<CourierMessage>> = repository.allMessages
    val timeRemaining: LiveData<String>
        get() = _timeRemaining

    private val _timeRemaining = MutableLiveData<String>()

    init {
        _timeRemaining.value = "00:00"
    }

    fun sendMessage(message: String) = viewModelScope.launch(Dispatchers.IO) {
        val currentDateTime = DateTime()
        val bidAcceptedDateTime = DateTime(sharedPrefs.getString("bid_accepted_datetime")!!)
        repository.sendMessage(
            CourierMessage(
                currentDateTime.getSplitSecondsSince(bidAcceptedDateTime),
                currentDateTime.getString(),
                currentDateTime.getTimestampString(com.annalisetarhan.kacaolur.Application.context),
                false,
                message
            )
        )
    }

    fun setUpCountdownTimer(context: Context) {
        val secondsRemaining = calculateCountdownTime()
        _timeRemaining.value = Time(secondsRemaining).getCountdownString(context)
        startCountdown(secondsRemaining, context)
    }

    private fun calculateCountdownTime(): Int {
        val timePromisedInSeconds = sharedPrefs.getInt("delivery_time_in_seconds")
        val timePausedInSeconds = sharedPrefs.getInt("time_paused_in_seconds")

        val dateTimeBidAccepted = DateTime(sharedPrefs.getString("bid_accepted_datetime")!!)
        val secondsSinceBidAccepted = dateTimeBidAccepted.secondsAgo()

        val timeRemaining = timePromisedInSeconds + timePausedInSeconds - secondsSinceBidAccepted
        return if (timeRemaining < 1) {
            0
        } else {
            timeRemaining
        }
    }

    private fun startCountdown(seconds: Int, context: Context) {
        object : CountDownTimer(seconds * 1000.toLong(), 1000) {

            override fun onTick(p0: Long) {
                val secondsRemaining = Time((p0 / 1000).toInt())
                _timeRemaining.value = secondsRemaining.getCountdownString(context)
            }

            override fun onFinish() {
                _timeRemaining.value = "00:00"
            }
        }.start()

        if (seconds == 0) {
            _timeRemaining.value = "00:00"
        }
    }

    fun pauseTimer(context: Context) {
        sharedPrefs.setBoolean("waiting_for_item_inspection", true)
        sharedPrefs.setString("last_time_paused", Time().getSharedPrefsString(context))
    }

    fun getCourierName() = sharedPrefs.getString("courier_name")
    fun getDeliveryPrice() = sharedPrefs.getFloat("delivery_price")

    fun getDeliveryTime(): String {
        val timeInSeconds = sharedPrefs.getInt("delivery_time_in_seconds")
        return Time(timeInSeconds).getTimeInMinutes()
    }

    fun isWaitingForItemInspection() =
        sharedPrefs.getBoolean("waiting_for_item_inspection")

    fun nukeData() {
        sharedPrefs.nuke()
    }
}