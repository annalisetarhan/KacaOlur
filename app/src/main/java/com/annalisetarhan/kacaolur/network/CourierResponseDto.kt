package com.annalisetarhan.kacaolur.network

import com.annalisetarhan.kacaolur.database.DatabaseCourierResponse
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCourierResponseContainer(val responses: List<NetworkCourierResponse>)

@JsonClass(generateAdapter = true)
data class NetworkCourierResponse(
    val splitSeconds: Int,
    val dateTimeSubmitted: String,
    val courierName: String,
    val bidNotQuestion: Boolean,
    val deliveryPrice: Float?,
    val deliveryTimeInSeconds: Int?,
    val question: String?,
    val answer: String?
)

fun NetworkCourierResponseContainer.asDatabaseModel(): List<DatabaseCourierResponse> {
    return responses.map {
        DatabaseCourierResponse(
            splitSeconds = it.splitSeconds,
            dateTimeSubmitted = it.dateTimeSubmitted,
            courierName = it.courierName,
            bidNotQuestion = it.bidNotQuestion,
            deliveryPrice = it.deliveryPrice,
            deliveryTimeInSeconds = it.deliveryTimeInSeconds,
            question = it.question,
            answer = it.answer,
            synced = true
        )
    }
}