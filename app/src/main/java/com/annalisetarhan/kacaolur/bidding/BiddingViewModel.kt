package com.annalisetarhan.kacaolur.bidding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annalisetarhan.kacaolur.storage.SharedPreferencesStorage
import com.annalisetarhan.kacaolur.utils.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BiddingViewModel @Inject constructor(
    private val repository: CourierResponseRepository,
    private val sharedPrefs: SharedPreferencesStorage
) : ViewModel() {

    val allEntries: LiveData<List<CourierResponse>> = repository.allEntries

    fun addAnswer(answer: String, splitSecondsSinceOrderPlaced: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.answerQuestion(answer, splitSecondsSinceOrderPlaced)
    }

    fun saveAcceptedBid(bid: CourierResponse) = viewModelScope.launch {
        writeBidToSharedPreferences(bid)
        repository.acceptBid(bid)
    }

    fun hasAcceptedBid(): Boolean {
        return sharedPrefs.getBoolean("has_accepted_bid")
    }

    fun getItemName(): String = sharedPrefs.getString("item_name") ?: error("Item name not in SharedPrefs")
    fun getItemDescription(): String = sharedPrefs.getString("item_description") ?: ("Item description not in SharedPrefs")

    private fun writeBidToSharedPreferences(bid: CourierResponse) {
        sharedPrefs.setString("courier_name", bid.courierName)
        sharedPrefs.setFloat("delivery_price", bid.deliveryPrice!!)
        sharedPrefs.setInt("delivery_time_in_seconds", bid.deliveryTimeInSeconds!!)
        sharedPrefs.setString("bid_accepted_datetime", DateTime().getString())
        sharedPrefs.setInt("time_paused_in_seconds", 0)
        sharedPrefs.setBoolean("has_accepted_bid", true)
    }
}