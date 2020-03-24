package com.annalisetarhan.kacaolur.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.annalisetarhan.kacaolur.bidding.CourierResponse

@Entity(tableName = "response_table")
data class DatabaseCourierResponse(
    @PrimaryKey
    @ColumnInfo(name = "split_seconds") val splitSeconds: Int,
    @ColumnInfo(name = "date_time_submitted") val dateTimeSubmitted: String,
    @ColumnInfo(name = "courier_name") val courierName: String,
    @ColumnInfo(name = "bid_not_question") val bidNotQuestion: Boolean,
    @ColumnInfo(name = "delivery_price") val deliveryPrice: Float?,
    @ColumnInfo(name = "delivery_time") val deliveryTimeInSeconds: Int?,
    @ColumnInfo(name = "question") val question: String?,
    @ColumnInfo(name = "answer") val answer: String?,
    @ColumnInfo(name = "synced") var synced: Boolean
)

/*
 * TODO: maybe?
 * Once a bid has been accepted, there is currently no way to mark it as such on the bidding fragment.
 * I can't see that being a problem, really, unless for some reason I wanted to pull current order
 * information from this table, which seems extremely unlikely.
 *
 * It's also possible that I'd want the customer to be able to see on the bid list (post-acceptance)
 * which one they accepted. At the moment, very low priority.
 */

fun List<DatabaseCourierResponse>.asDomainModel(): List<CourierResponse> {
    return map {
        CourierResponse(
            splitSecondsSinceOrderPlaced = it.splitSeconds,
            dateTimeSubmitted = it.dateTimeSubmitted,
            courierName = it.courierName,
            bidNotQuestion = it.bidNotQuestion,
            deliveryPrice = it.deliveryPrice,
            deliveryTimeInSeconds = it.deliveryTimeInSeconds,
            question = it.question,
            answer = it.answer
        )
    }
}