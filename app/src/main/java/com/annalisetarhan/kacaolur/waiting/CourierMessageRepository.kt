package com.annalisetarhan.kacaolur.waiting

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.annalisetarhan.kacaolur.database.CourierMessageDao
import com.annalisetarhan.kacaolur.database.DatabaseCourierMessage
import com.annalisetarhan.kacaolur.database.asDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourierMessageRepository @Inject constructor(
    private val courierMessageDao: CourierMessageDao
) {
    val allMessages: LiveData<List<CourierMessage>> =
        Transformations.map(courierMessageDao.getAllEntries()) {
            it.asDomainModel()
    }

    @WorkerThread
    suspend fun sendMessage(message: CourierMessage) {
        courierMessageDao.sendMessage(
            DatabaseCourierMessage(
                dateTimeSent = message.dateTimeSent,
                splitSeconds = message.splitSecondsSinceBidAccepted,
                timeStamp = message.timeStamp,
                fromCourier = message.fromCourier,
                text = message.text,
                synced = false
            )
        )
        // TODO: send message to server
    }
}