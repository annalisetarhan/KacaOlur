package com.annalisetarhan.kacaolur.bidding

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.annalisetarhan.kacaolur.database.CourierResponseDao
import com.annalisetarhan.kacaolur.database.asDomainModel
import com.annalisetarhan.kacaolur.network.CourierResponseService
import com.annalisetarhan.kacaolur.storage.SharedPreferencesStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourierResponseRepository @Inject constructor(
    private val courierResponseService: CourierResponseService,
    private val courierResponseDao: CourierResponseDao,
    private val sharedPrefs: SharedPreferencesStorage
) {
    val allEntries: LiveData<List<CourierResponse>> =
        Transformations.map(courierResponseDao.getAllEntries()) {
            it.asDomainModel()
        }

    @WorkerThread
    suspend fun answerQuestion(answer: String, splitSecondsSinceOrderPlaced: Int) {
        val requestId = sharedPrefs.getString("request_id") ?: error("request_id missing from SharedPrefs")
        courierResponseDao.addAnswer(answer, splitSecondsSinceOrderPlaced)
        courierResponseService.answerQuestion(
            requestId,
            mapOf("answer" to answer, "splitSeconds" to splitSecondsSinceOrderPlaced.toString())
        )
    }

    @WorkerThread
    suspend fun acceptBid(bid: CourierResponse) {
        val requestId = sharedPrefs.getString("request_id") ?: error("request_id missing from SharedPrefs")
        val orderId = courierResponseService.acceptBidAsync(requestId, bid.splitSecondsSinceOrderPlaced).await()
        sharedPrefs.setString("order_id", orderId)
    }
}