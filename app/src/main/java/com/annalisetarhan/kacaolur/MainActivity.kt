package com.annalisetarhan.kacaolur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected

class MainActivity : AppCompatActivity() {

    private val userID = "user_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.settingsFragment) {
            item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
