package com.annalisetarhan.kacaolur.waiting

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_thread")
data class CourierMessage(
    @PrimaryKey(autoGenerate = true) val messageNum: Int,
    @ColumnInfo(name = "from_courier") val fromCourier: Boolean,
    @ColumnInfo(name = "time_stamp") val timeStamp: String,
    @ColumnInfo(name = "message") val message: String
)