package com.hireme.android.library.core.aqi.data

/**
 * Represents a reading for a forecast for a single day.
 */
data class ForecastReading(

    val day: String = "",

    val max: Int = 0,

    val min: Int = 0,

    val average: Int = 0
)