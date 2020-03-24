package com.annalisetarhan.kacaolur.welcoming

import androidx.lifecycle.ViewModel
import com.annalisetarhan.kacaolur.storage.SharedPreferencesStorage
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    sharedPrefs: SharedPreferencesStorage
): ViewModel() {
    private var userIsLoggedIn: Boolean

    init {
        val phoneNumber = sharedPrefs.getString("phone_number")
        val phoneNumberValidated = sharedPrefs.getBoolean("phone_number_validated")
        val username = sharedPrefs.getString("username")
        val userId = sharedPrefs.getString("userId")
        userIsLoggedIn =
            (phoneNumber != null && phoneNumberValidated && username != null && userId != null)
    }

    fun userIsLoggedIn(): Boolean {
        return userIsLoggedIn
    }
}