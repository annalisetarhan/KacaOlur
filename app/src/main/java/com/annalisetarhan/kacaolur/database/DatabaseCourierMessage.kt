package com.annalisetarhan.kacaolur.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.annalisetarhan.kacaolur.waiting.CourierMessage

@Entity(tableName = "message_thread")
data class DatabaseCourierMessage(
    @PrimaryKey
    @ColumnInfo(name = "split_seconds") val splitSeconds: Int,
    @ColumnInfo(name = "date_time_sent") val dateTimeSent: String,
    @ColumnInfo(name = "time_stamp") val timeStamp: String,
    @ColumnInfo(name = "from_courier") val fromCourier: Boolean,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "synced") var synced: Boolean
)

fun List<DatabaseCourierMessage>.asDomainModel(): List<CourierMessage> {
    return map {
        CourierMessage(
            dateTimeSent = it.dateTimeSent,
            splitSecondsSinceBidAccepted = it.splitSeconds,
            timeStamp = it.timeStamp,
            fromCourier = it.fromCourier,
            text = it.text
        )
    }
}

// TODO: if a message can't be sent due to a network problem, let the user know