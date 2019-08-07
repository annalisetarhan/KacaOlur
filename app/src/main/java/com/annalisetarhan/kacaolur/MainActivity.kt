package com.annalisetarhan.kacaolur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {

    private val userID = "user_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Shared Preferences
        val sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs_filename), 0)

        if (sharedPreferences.getLong(userID, 0) != 0L) {
            setContentView(R.layout.activity_main)
        } else {
            // TODO Implement Login fragment
            val editor = sharedPreferences.edit()
            editor.putLong(userID, 1)
            editor.apply()
            setContentView(R.layout.activity_main)
        }
    }
}
