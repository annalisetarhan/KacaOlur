package com.annalisetarhan.kacaolur.bidding

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class BidTableEntryRepository(private val bidTableEntryDao: BidTableEntryDao) {
    val allEntries: LiveData<List<BidTableEntry>> = bidTableEntryDao.getAllEntries()

    @WorkerThread
    suspend fun insert(entry: BidTableEntry) {
        bidTableEntryDao.insert(entry)
    }
}