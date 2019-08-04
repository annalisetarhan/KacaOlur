package com.annalisetarhan.kacaolur.waiting

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CourierMessageDao {

    @Insert
    suspend fun sendMessage(courierMessage: CourierMessage)

    @Query("SELECT * from message_thread ORDER BY messageNum ASC")
    fun getAllEntries() : LiveData<List<CourierMessage>>

    @Query("DELETE FROM message_thread")
    fun deleteAll()
}