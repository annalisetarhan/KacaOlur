package com.annalisetarhan.kacaolur.authing

import javax.inject.Inject

class FakeUserService @Inject constructor() {
    fun getUserInfo(phoneNum: String) = emptyMap<String, String>()
    fun checkUsernameAvailability(username: String) = true
    fun saveUsername(username: String, phoneNum: String) = "89080989"

}