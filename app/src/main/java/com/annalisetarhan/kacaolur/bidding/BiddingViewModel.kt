package com.annalisetarhan.kacaolur.bidding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.database.CourierResponseRoomDatabase
import com.annalisetarhan.kacaolur.utils.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BiddingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
        CourierResponseRepository(CourierResponseRoomDatabase.getDatabase(application, viewModelScope))
    val allEntries: LiveData<List<CourierResponse>>

    init {
        allEntries = repository.allEntries
    }

    fun addAnswer(answer: String, splitSecondsSinceOrder: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.addAnswer(answer, splitSecondsSinceOrder)
    }

    fun saveAcceptedBid(bid: CourierResponse) {
        writeBidToSharedPreferences(bid)
        // TODO: tell server what's going on
    }

    private fun writeBidToSharedPreferences(bid: CourierResponse) {
        val context = getApplication<Application>().applicationContext
        val sharedPrefs = context.getSharedPreferences((R.string.shared_prefs_filename).toString(), 0)
        val editor = sharedPrefs.edit()
        editor.putString("courier_name", bid.courierName)
        editor.putFloat("delivery_price", bid.deliveryPrice!!)
        editor.putInt("delivery_time_in_seconds", bid.deliveryTimeInSeconds!!)
        editor.putString("bid_accepted_datetime_string", DateTime().getString())
        editor.putInt("time_paused_in_seconds", 0)
        editor.putBoolean("has_accepted_bid", true)
        editor.apply()
    }
}