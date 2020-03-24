package com.annalisetarhan.kacaolur.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface CourierMessageService {
    @GET("couriermessages")
    fun getMessages(userId: String): Deferred<NetworkCourierMessageContainer>
}