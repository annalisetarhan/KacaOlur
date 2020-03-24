package com.annalisetarhan.kacaolur.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.annalisetarhan.kacaolur.database.DatabaseCourierMessage

@Dao
interface CourierMessageDao {

    @Insert
    suspend fun sendMessage(databaseCourierMessage: DatabaseCourierMessage)

    @Query("SELECT * from message_thread ORDER BY split_seconds ASC")
    fun getAllEntries() : LiveData<List<DatabaseCourierMessage>>

    @Query("DELETE FROM message_thread")
    suspend fun deleteAll()
}