package com.annalisetarhan.kacaolur.bidding

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bid_table")
data class BidTableEntry(
    @PrimaryKey val time_stamp: Int,
    @ColumnInfo(name = "courier_name") val courierName: String,
    @ColumnInfo(name = "bid_not_question") val bidNotQuestion: Boolean,
    @ColumnInfo(name = "delivery_price") val deliveryPrice: Float?,
    @ColumnInfo(name = "delivery_time") val deliveryTime: Int?,
    @ColumnInfo(name = "question") val question: String?
)