package com.annalisetarhan.kacaolur

import android.content.Context
import org.threeten.bp.LocalTime


class Time {
    private var time: LocalTime
    private var hours = -1
    private var minutes = -1
    private var seconds = -1

    constructor() {
        time = LocalTime.now()
        setHMS()
    }

    constructor(time: LocalTime) {
        this.time = time
        setHMS()
    }

    constructor(time: String) {
        this.time = LocalTime.parse(time)
        setHMS()
    }

    constructor(numSeconds: Int) {
        hours = numSeconds / 3600
        minutes = (numSeconds % 3600) / 60
        seconds = numSeconds % 60

        time = LocalTime.of(hours, minutes, seconds)
    }

    private fun setHMS() {
        hours = time.hour
        minutes = time.minute
        seconds = time.second
    }

    fun getStringForCountdown(context: Context): String {
        val minString = twoDigitString(minutes, context)
        val secString = twoDigitString(seconds, context)

        return if (hours == 0) {
            context.resources.getString(R.string.mm_ss, minString, secString)
        } else {
            val hourString = twoDigitString(hours, context)
            context.resources.getString(R.string.hh_mm_ss, hourString, minString, secString)
        }
    }

    fun getTimestampString(context: Context): String {
        val hourString = twoDigitString(hours, context)
        val minuteString = twoDigitString(minutes, context)
        return context.resources.getString(R.string.hh_mm, hourString, minuteString)
    }

    fun getTimeInMinutes(): String {
        val totalMinutes = 60*hours + minutes
        return totalMinutes.toString()
    }

    private fun twoDigitString(num: Int, context: Context): String {
        return if (num < 10) {
            context.resources.getString(R.string.add_zero, num)
        } else {
            num.toString()
        }
    }

    fun getSeconds(): Int {
        return (hours*3600) + (minutes*60) + seconds
    }
}