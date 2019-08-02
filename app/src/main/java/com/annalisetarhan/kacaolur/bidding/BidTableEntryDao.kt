package com.annalisetarhan.kacaolur.bidding

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BidTableEntryDao {

    @Insert
    suspend fun insert(bidTableEntry: BidTableEntry)

    @Query( "SELECT * from bid_table ORDER BY rowNum ASC")
    fun getAllEntries() : LiveData<List<BidTableEntry>>

    @Query("UPDATE bid_table SET answer = :answer WHERE rowNum = :rowId ")
    fun addAnswer(answer: String, rowId: Int)

    @Delete
    suspend fun delete(bidTableEntry: BidTableEntry)

    @Query("DELETE FROM bid_table")
    fun deleteAll()
}