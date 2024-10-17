package com.hireme.android.library.core.networking.extensions

import com.hireme.android.library.core.networking.api.data.AqiApiStatusCodes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.isStatusOk(): Boolean = this.contentEquals(AqiApiStatusCodes.OK)

fun String.isStatusError(): Boolean = this.contentEquals(AqiApiStatusCodes.ERROR)

fun String.timestampToDay(): String {

    // Define the input date format (which includes both date and time)
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    // Parse the timestamp into a Date object
    val dateTime: Date = inputFormat.parse(this)

    // Define the output format to extract only the date
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Format the Date object to get just the date as a String
    val day = outputFormat.format(dateTime)

    return day
}