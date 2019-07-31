package com.annalisetarhan.kacaolur.bidding

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BidTableEntryDao {

    @Insert
    suspend fun insert(bidTableEntry: BidTableEntry)

    @Query( "SELECT * from bid_table ORDER BY time_stamp ASC")
    fun getAllEntries() : LiveData<List<BidTableEntry>>

    @Delete
    suspend fun delete(bidTableEntry: BidTableEntry)

    @Query("DELETE FROM bid_table")
    fun deleteAll()
}