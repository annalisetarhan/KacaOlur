package com.annalisetarhan.kacaolur.bidding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BiddingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BidTableEntryRepository
    val allEntries: LiveData<List<BidTableEntry>>

    init {
        // This is how you access the room database!! You just need a reference to the application (context?)
        val entriesDao = BidTableEntryRoomDatabase.getDatabase(application, viewModelScope).bidTableEntryDao()
        // I think this implies that the room database persists, but the viewModel creates new repositories every time
        repository = BidTableEntryRepository(entriesDao)
        allEntries = repository.allEntries
    }

    fun addAnswer(answer: String, rowId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.addAnswer(answer, rowId)
    }

    fun saveAcceptedBid(entry: BidTableEntry) {
        writeBidToSharedPreferences(entry)
        // TODO: tell server what's going on
    }

    private fun writeBidToSharedPreferences(entry: BidTableEntry) {
        val context = getApplication<Application>().applicationContext
        val sharedPrefs = context.getSharedPreferences((R.string.shared_prefs_filename).toString(), 0)
        val editor = sharedPrefs.edit()
        editor.putString("courier_name", entry.courierName)
        editor.putFloat("delivery_price", entry.deliveryPrice!!)
        editor.putInt("delivery_time_in_seconds", entry.deliveryTimeInSeconds!!)
        editor.putString("time_bid_accepted_string", Time().getStringForSharedPrefs(context))
        editor.putInt("time_paused_in_seconds", 0)
        editor.putBoolean("has_accepted_bid", true)
        editor.apply()
    }
}