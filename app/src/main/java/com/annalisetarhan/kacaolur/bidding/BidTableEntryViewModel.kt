package com.annalisetarhan.kacaolur.bidding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BidTableEntryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BidTableEntryRepository
    val allEntries: LiveData<List<BidTableEntry>>

    init {
        // This is how you access the room database!! You just need a reference to the application (context?)
        val entriesDao = BidTableEntryRoomDatabase.getDatabase(application, viewModelScope).bidTableEntryDao()
        // I think this implies that the room database persists, but the viewModel creates new repositories every time
        repository = BidTableEntryRepository(entriesDao)
        allEntries = repository.allEntries
    }

    // This is a wrapper method for the repository's insert() method
    // Using the IO Dispatcher because this is a database operation
    fun insert(entry: BidTableEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entry)
    }

    // Did this one all on my own. Still feels a bit silly and redundant, but I'm going with it.
    fun addAnswer(answer: String, rowId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.addAnswer(answer, rowId)
    }
}