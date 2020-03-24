package com.annalisetarhan.kacaolur.network

import android.content.Context
import com.annalisetarhan.kacaolur.R
import com.annalisetarhan.kacaolur.database.DatabaseCourierMessage
import com.annalisetarhan.kacaolur.utils.DateTime
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class NetworkCourierMessageContainer(val messages: List<NetworkCourierMessage>)

@JsonClass(generateAdapter = true)
data class NetworkCourierMessage(
    val splitSeconds: Int,
    val dateTimeSent: String,
    val fromCourier: Boolean,
    val text: String
)

// Only call this if sharedPrefs knows about the accepted bid (should I be passing that info around as well as context, just to be sure?)
fun NetworkCourierMessageContainer.asDatabaseModel(context: Context): List<DatabaseCourierMessage> {
    return messages.map {
        DatabaseCourierMessage(
            splitSeconds = it.splitSeconds,
            dateTimeSent = it.dateTimeSent,
            timeStamp = getTimeStampFromTimeSent(it.dateTimeSent, context),
            fromCourier = it.fromCourier,
            text = it.text,
            synced = true
        )
    }
}

// This should only be called if sharedPrefs definitely has the accepted bid stored
private fun getDateTimeBidAccepted(context: Context): DateTime {
    val sharedPrefs = context.getSharedPreferences((R.string.shared_prefs_filename).toString(), 0)
    return DateTime(sharedPrefs.getString("bid_accepted_datetime", "")!!)
}

private fun getTimeStampFromTimeSent(dateTimeSent: String, context: Context): String {
    val dateTime = DateTime(dateTimeSent)
    return dateTime.getTimestampString(context)
}
