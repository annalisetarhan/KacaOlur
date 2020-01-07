package com.annalisetarhan.kacaolur.bidding

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "bid_table")
data class BidTableEntry(
    @PrimaryKey(autoGenerate = true) var rowNum: Int,
    @ColumnInfo(name = "courier_name") val courierName: String,
    @ColumnInfo(name = "bid_not_question") val bidNotQuestion: Boolean,
    @ColumnInfo(name = "delivery_price") val deliveryPrice: Float?,
    @ColumnInfo(name = "delivery_time") val deliveryTimeInSeconds: Int?,
    @ColumnInfo(name = "question") val question: String?,
    @ColumnInfo(name = "answer") val answer: String?
)

// Probably going to want to add time_submitted