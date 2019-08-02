package com.annalisetarhan.kacaolur.bidding

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class BidTableEntryRepository(private val bidTableEntryDao: BidTableEntryDao) {

    val allEntries: LiveData<List<BidTableEntry>> = bidTableEntryDao.getAllEntries()

    // WorkerThread means this needs to be called from a non-ui thread
    // Suspend means it needs to be called from a coroutine or another suspending function
    @WorkerThread
    fun addAnswer(answer: String, rowId: Int) {
        bidTableEntryDao.addAnswer(answer, rowId)
    }
}