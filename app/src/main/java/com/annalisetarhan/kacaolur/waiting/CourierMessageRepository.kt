package com.annalisetarhan.kacaolur.waiting

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class CourierMessageRepository(private val courierMessageDao: CourierMessageDao) {
    val allMessages: LiveData<List<CourierMessage>> = courierMessageDao.getAllEntries()

    @WorkerThread
    suspend fun sendMessage(message: CourierMessage) {
        courierMessageDao.sendMessage(message)
    }
}