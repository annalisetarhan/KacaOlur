package com.annalisetarhan.kacaolur.network

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface CourierResponseService {
    @GET("/requests/{id}/responses")
    fun getResponses(
        @Path("id") userId: String
    ): Deferred<NetworkCourierResponseContainer>

    @PATCH("/requests/{id}/questions")
    fun answerQuestion(
        @Path("id") requestId: String,
        @Body answerMap: Map<String, String> // should be "splitSeconds" and "answer"
    )

    @PATCH("/requests/bids/{id}")
    fun acceptBidAsync(
        @Path("id") requestId: String,
        @Body splitSecondsSinceOrderPlaced: Int
    ): Deferred<String>
}
