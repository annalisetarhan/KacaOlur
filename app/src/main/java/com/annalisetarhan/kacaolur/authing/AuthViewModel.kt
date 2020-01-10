package com.annalisetarhan.kacaolur.authing

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.annalisetarhan.kacaolur.R

class AuthViewModel(application: Application): AndroidViewModel(application) {

    private val sharedPrefs: SharedPreferences
    var phoneNumber: String?
    var userIsLoggedIn = false
    var phoneNumberValidated = false
    var username: String?

    init {
        val context = getApplication<Application>().applicationContext
        sharedPrefs = context.getSharedPreferences((R.string.shared_prefs_filename).toString(), 0)
        phoneNumber = sharedPrefs.getString("phone_number", null)
        phoneNumberValidated = sharedPrefs.getBoolean("phone_number_validated", false)
        username = sharedPrefs.getString("username", null)
        userIsLoggedIn = (phoneNumber != null && username != null)
    }

    fun savePhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        val editor = sharedPrefs.edit()
        editor.putString("phone_number", phoneNumber)
        editor.apply()
    }

    fun selectStage() : Int {
        return when {
            userIsLoggedIn -> 6
            phoneNumberValidated -> 4
            phoneNumber != null -> 2
            else -> 1
        }
    }

    fun codeIsValid(
        code: String) : Boolean {
        // Do fancy google stuff
        validatePhoneNumber()
        getUserInfo()
        return true
    }

    private fun validatePhoneNumber() {
        phoneNumberValidated = true
        val editor = sharedPrefs.edit()
        editor.putBoolean("phone_number_validated", true)
        editor.apply()
    }

    private fun getUserInfo() {
        // Send server the phone number, see if we know about them
        userIsLoggedIn = false
    }

    fun nameIsAvailable(username: String) : Boolean {           // Is it bad practice to save username in a function whose purpose is to check availability?
        // Send username to server, see if it's already in use
        if (true) {
            this.username = username
            val editor = sharedPrefs.edit()
            editor.putString("username", username)
            editor.apply()
            return true
        } else {
            return false
        }
    }

    fun nukeData() {
        sharedPrefs.edit().clear().apply()
    }
}