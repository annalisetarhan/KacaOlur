package com.annalisetarhan.kacaolur.utils

import android.content.Context
import org.threeten.bp.LocalDateTime

class DateTime {
    private var dateTime: LocalDateTime

    constructor() {
        dateTime = LocalDateTime.now()
    }

    constructor(dateTime: LocalDateTime) {
        this.dateTime = dateTime
    }

    constructor(dateTimeString: String) {
        dateTime = LocalDateTime.parse(dateTimeString)
    }

    fun getString(): String {
        return dateTime.toString()
    }

    fun secondsAgo(): Int {
        val curTime = Time()
        val pastTime = Time(dateTime.toLocalTime())
        return curTime.secondsSince(pastTime)
    }

    private fun secondsSince(earlierDateTime: DateTime): Int {
        val earlierTime = Time(earlierDateTime.dateTime.toLocalTime())
        val laterTime = Time(dateTime.toLocalTime())
        return laterTime.secondsSince(earlierTime)
    }

    fun getSplitSecondsSince(earlierDateTime: DateTime): Int {
        val secondsSince = secondsSince(earlierDateTime)
        val truncatedNanos = getNanos().toString().substring(0, 4).toInt()
        return secondsSince * 10000 + truncatedNanos
    }

    private fun getNanos(): Int {
        return dateTime.nano
    }

    fun getTimestampString(context: Context): String {
        val time = Time(dateTime.toLocalTime())
        return time.getTimestampString(context)
    }
}