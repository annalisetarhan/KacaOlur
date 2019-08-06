package com.annalisetarhan.kacaolur.confirming

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.Time

class ConfirmingViewModel : ViewModel() {

    lateinit var sharedPrefs: SharedPreferences

    fun unPauseTimer(context: Context) {
        sharedPrefs = context.getSharedPreferences(R.string.shared_prefs_filename.toString(), 0)

        val secondsPaused = getSecondsPaused()

        val editor = sharedPrefs.edit()
        editor.putInt("time_paused_in_seconds", secondsPaused)
        println("put " + secondsPaused + " in sharedPrefs")
        editor.putString("last_time_paused", "")
        editor.putBoolean("waiting_for_item_inspection", false)
        editor.apply()
    }

    private fun getSecondsPaused(): Int {
        val currentTime = Time()
        val lastTimePaused = sharedPrefs.getString("last_time_paused", "")!!

        val timePausedThisTime =
            if (lastTimePaused == "") {
                0
            } else {
                currentTime.secondsSince(Time(lastTimePaused))
            }

        val timePausedPreviously = sharedPrefs.getInt("time_paused_in_seconds", 0)
        return timePausedThisTime + timePausedPreviously
    }
}