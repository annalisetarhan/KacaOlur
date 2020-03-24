package com.annalisetarhan.kacaolur.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CourierResponseDao {

    // Should they all be suspend funs? I have no idea.

    @Insert
    suspend fun insert(databaseResponse: DatabaseCourierResponse)

    @Query( "SELECT * from response_table ORDER BY split_seconds ASC")
    fun getAllEntries() : LiveData<List<DatabaseCourierResponse>>

    @Query("UPDATE response_table SET answer = :answer, synced = 0 WHERE split_seconds = :splitSecondsSinceOrder")
    suspend fun addAnswer(answer: String, splitSecondsSinceOrder: Int)

    @Delete
    suspend fun delete(databaseResponse: DatabaseCourierResponse)

    @Query("DELETE FROM response_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM response_table WHERE split_seconds = :splitSecondsSinceOrder")
    suspend fun getEntry(splitSecondsSinceOrder: Int): DatabaseCourierResponse
}