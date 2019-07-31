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
        val entriesDao = BidTableEntryRoomDatabase.getDatabase(application).bidTableEntryDao()
        repository = BidTableEntryRepository(entriesDao)
        allEntries = repository.allEntries
    }

    // This is a wrapper method for the repository's insert() method
    // Using the IO Dispatcher because this is a database operation
    fun insert(entry: BidTableEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entry)
    }
}

// MEMORY LEAK WARNING
// Activities, fragments, and views have shorter lifecycles than ViewModels
// => Don't keep references to them