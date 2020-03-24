package com.annalisetarhan.kacaolur.waiting

data class CourierMessage (
    val splitSecondsSinceBidAccepted: Int,
    val dateTimeSent: String,
    val timeStamp: String,
    val fromCourier: Boolean,
    val text: String
)