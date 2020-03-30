package com.annalisetarhan.kacaolur.network

import retrofit2.http.*

interface UserService {
    @GET("/users/{phone_num}")
    suspend fun getUserInfo(
        @Path("phone_num") phoneNum: String
    ): Map<String, String>

    @HEAD("/users/{username}")
    suspend fun checkUsernameAvailability(
        @Path("username") username: String
    ): Boolean

    @POST("/users/{username}")
    suspend fun saveUsername(
        @Path("username") username: String,
        @Body phoneNum: String
    ): String
}