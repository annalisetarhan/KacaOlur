package com.annalisetarhan.kacaolur.bidding

// TODO: sealed class?
data class CourierResponse (
    val splitSecondsSinceOrderPlaced: Int,
    val dateTimeSubmitted: String,
    val courierName: String,
    val bidNotQuestion: Boolean,
    val deliveryPrice: Float?,
    val deliveryTimeInSeconds: Int?,
    val question: String?,
    val answer: String?
)