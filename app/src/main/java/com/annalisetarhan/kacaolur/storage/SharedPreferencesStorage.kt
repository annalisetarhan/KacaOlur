package com.annalisetarhan.kacaolur.storage

import android.content.Context
import com.annalisetarhan.kacaolur.R
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(context: Context): Storage {
    private val sharedPreferences = context.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)

    override fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun setBoolean(key: String, value: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    override fun setInt(key: String, value: Int) {
        with(sharedPreferences.edit()) {
            putInt(key, value)
            apply()
        }
    }

    override fun setFloat(key: String, value: Float) {
        with(sharedPreferences.edit()) {
            putFloat(key, value)
            apply()
        }
    }

    override fun getString(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    override fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    override fun getFloat(key: String): Float {
        return sharedPreferences.getFloat(key, 0f)
    }

    override fun nuke() {
        sharedPreferences.edit().clear().apply()
    }
}