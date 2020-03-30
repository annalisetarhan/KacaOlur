package com.annalisetarhan.kacaolur.authing

import androidx.annotation.WorkerThread
import com.annalisetarhan.kacaolur.network.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    //private val userService: UserService
    private val userService: FakeUserService
) {

    // Note: all the suspends are necessary with the real userService

    @WorkerThread
    suspend fun checkCode(code: String): Boolean {
        // TODO: firebase
        return true
    }

    // Hopefully, the map will contain username and userId
    @WorkerThread
    suspend fun getUserInfo(phoneNum: String): Map<String, String> {
        return try {
            val info = userService.getUserInfo(phoneNum)
            info
        } catch (e: Exception) {
            println("Exception in UserRepository getUserInfo(): $e")
            emptyMap()
        }
    }

    @WorkerThread
    suspend fun checkUsernameAvailability(username: String): Boolean {
        return try {
            userService.checkUsernameAvailability(username)
        } catch (e: Exception) {
            println("Error in UserRepository checkUsernameAvailability(): $e")
            false
        }
    }

    @WorkerThread
    suspend fun actuallySaveUsername(username: String, phoneNum: String): String? {
        return try {
            userService.saveUsername(username, phoneNum)
        } catch (e: Exception) {
            println("Error in UserRepository actuallySaveUsername(): $e")
            return null
        }

    }
}