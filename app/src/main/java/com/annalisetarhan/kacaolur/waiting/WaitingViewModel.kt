package com.annalisetarhan.kacaolur.waiting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalTime


class WaitingViewModel(application: Application): AndroidViewModel(application) {
    private val repository: CourierMessageRepository
    val allMessages: LiveData<List<CourierMessage>>

    init {
        val messagesDao = CourierMessageRoomDatabase.getDatabase(application, viewModelScope).courierMessageDao()
        repository = CourierMessageRepository(messagesDao)
        allMessages = repository.allMessages
    }

    fun sendMessage(message: String) = viewModelScope.launch(Dispatchers.IO) {
        val time = getTime()
        val courierMessage = CourierMessage(0, false, time, message)
        repository.sendMessage(courierMessage)
    }

    private fun getTime(): String {
        val clock = LocalTime.now()
        val currentHour = clock.hour.toString()
        val currentMinute =
            if (clock.minute < 10) {
                "0" + clock.minute.toString()
            } else {
                clock.minute.toString()
            }
        return currentHour + ":" + currentMinute
    }
}