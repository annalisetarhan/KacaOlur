package com.annalisetarhan.kacaolur.authing

import androidx.lifecycle.ViewModel
import com.annalisetarhan.kacaolur.storage.SharedPreferencesStorage
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val sharedPrefs: SharedPreferencesStorage
) : ViewModel() {

    // TODO: User will get kicked to this page if their userId isn't in SharedPrefs, but there's nothing here to make sure we get it

    private var phoneNumber: String?
    private var userIsLoggedIn = false
    private var phoneNumberValidated = false
    var username: String?

    init {
        phoneNumber = sharedPrefs.getString("phone_number")
        phoneNumberValidated = sharedPrefs.getBoolean("phone_number_validated")
        username = sharedPrefs.getString("username")
        userIsLoggedIn = (phoneNumber != null && username != null)
    }

    fun savePhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        sharedPrefs.setString("phone_number", phoneNumber)
    }

    fun selectStage() : Int {
        return when {
            userIsLoggedIn -> 6
            phoneNumberValidated -> 4
            phoneNumber != null -> 2
            else -> 1
        }
    }

    fun codeIsValid(code: String) : Boolean {
        // TODO: send code to firebase to validate
        validatePhoneNumber()
        getUserInfo()
        return true
    }

    fun validatePhoneNumber() {
        phoneNumberValidated = true
        sharedPrefs.setBoolean("phone_number_validated", true)
        getUserInfo()
    }

    private fun getUserInfo() {
        // TODO: Send server the phone number, see if we know about them (is this all I need to do to fix the userId issue?)
    }

    fun nameIsAvailable(username: String) : Boolean {
        // TODO: Send username to server, see if it's already in use
        return username != "Mehmet"
    }

    fun saveUsername(username: String) {
        this.username = username
        sharedPrefs.setString("username", username)
    }

    fun userIsLoggedIn() {
        userIsLoggedIn = true
    }

    fun nukeData() {
        sharedPrefs.nuke()
    }


}