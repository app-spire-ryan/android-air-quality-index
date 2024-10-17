package com.hireme.android.library.core.networking.api.data.extensions

import com.hireme.android.library.core.networking.api.data.AqiTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Converts a timestamp from the API to the day as a String.
 *
 * @return [String]
 */
fun AqiTime.timestampToDay(): String {

    // Define the input date format (which includes both date and time)
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    // Parse the timestamp into a Date object
    val dateTime: Date = inputFormat.parse(this.timestamp)

    // Define the output format to extract only the date
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Format the Date object to get just the date as a String
    val day = outputFormat.format(dateTime)

    return day
}