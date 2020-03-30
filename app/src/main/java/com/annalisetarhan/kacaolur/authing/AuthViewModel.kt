package com.annalisetarhan.kacaolur.authing

import androidx.lifecycle.*
import com.annalisetarhan.kacaolur.storage.SharedPreferencesStorage
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val sharedPrefs: SharedPreferencesStorage,
    private val userRepository: UserRepository
) : ViewModel() {

    private var phoneNumber: String?
    private var userIsLoggedIn = false
    private var phoneNumberValidated = false
    private var userId: String?
    private var username: String?

    private val _currentStage = MutableLiveData<Int>()
    private val _loading = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()

    val currentStage: LiveData<Int>
        get() = _currentStage
    val loading: LiveData<Boolean>
        get() = _loading
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        phoneNumber = sharedPrefs.getString("phone_number")
        phoneNumberValidated = sharedPrefs.getBoolean("phone_number_validated")
        username = sharedPrefs.getString("username")
        userId = sharedPrefs.getString("user_id")
        userIsLoggedIn = (!phoneNumber.isNullOrEmpty() && !username.isNullOrEmpty() && !userId.isNullOrEmpty())

        _currentStage.value = selectStage()
        _loading.value = false
        _errorMessage.value = ""
    }

    /*
     *  STATE MANAGEMENT
     */

    private fun selectStage() : Int {
        return when {
            userIsLoggedIn -> 6
            phoneNumberValidated -> 4
            !phoneNumber.isNullOrEmpty() -> 2
            else -> 1
        }
    }

    fun updateStage(stage: Int) {
        _currentStage.value = stage
    }

    fun messageReceived() {
        _errorMessage.value = ""
    }

    fun getUsername() = username

    fun nukeData() {
        sharedPrefs.nuke()
    }

    /*
     *  REPO FUNCTIONS
     */

    fun savePhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        sharedPrefs.setString("phone_number", phoneNumber)
        _currentStage.value = 2
    }

    fun checkCode(code: String) {
        viewModelScope.launch {
            _loading.value = true
            val valid = userRepository.checkCode(code)
            if (valid) {
                sharedPrefs.setBoolean("phone_number_validated", true)
                phoneNumberValidated = true
                getUserInfo()
            } else {
                _errorMessage.value = "Invalid code"
            }
            _currentStage.value = selectStage()
            _loading.value = false
        }
    }

    private suspend fun getUserInfo() {
        val info = userRepository.getUserInfo(sharedPrefs.getString("phone_number")!!)
        // This should be obvious, but server shouldn't return map without values
        if (info.containsKey("username") && info.containsKey("userId")) {
            sharedPrefs.setString("username", info["username"] ?: error("Empty username"))
            sharedPrefs.setString("userId", info["userId"] ?: error("Empty userId"))
            username = info["username"]
            userId = info["userId"]
            userIsLoggedIn = true
        }
    }

    fun tryToSaveUsername(name: String) {
        viewModelScope.launch {
            _loading.value = true
            val nameIsAvailable = userRepository.checkUsernameAvailability(name)
            if (nameIsAvailable) {
                actuallySaveUsername(name)
            } else {
                _errorMessage.value = "Sorry, username is not available" // TODO: or, there was a network error
            }
            _currentStage.value = selectStage()
            _loading.value = false
        }
    }

    private suspend fun actuallySaveUsername(name: String) {
        val id = userRepository.actuallySaveUsername(name, phoneNumber!!)
        if (id == null) {
            _errorMessage.value = "Oops, something went wrong"
        } else {
            sharedPrefs.setString("username", name)
            sharedPrefs.setString("userId", id)
            username = name
            userId = id
            userIsLoggedIn = true
        }
    }
}