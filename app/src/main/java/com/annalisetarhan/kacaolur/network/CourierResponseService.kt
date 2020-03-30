package com.annalisetarhan.kacaolur.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface CourierResponseService {
    @GET("/requests/{id}/responses")
    suspend fun getResponses(
        @Path("id") userId: String
    ): NetworkCourierResponseContainer

    @PATCH("/requests/{id}/questions")
    suspend fun answerQuestion(
        @Path("id") requestId: String,
        @Body answerMap: Map<String, String> // should be "splitSeconds" and "answer"
    )

    @PATCH("/requests/bids/{id}")
    suspend fun acceptBidAsync(
        @Path("id") requestId: String,
        @Body splitSecondsSinceOrderPlaced: Int
    ): String
}
