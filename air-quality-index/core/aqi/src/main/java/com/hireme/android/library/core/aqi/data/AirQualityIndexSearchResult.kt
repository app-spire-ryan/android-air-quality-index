package com.hireme.android.library.core.aqi.data

/**
 * Represents a search result for a station that provides AQI information.
 */
data class AirQualityIndexSearchResult(

    /**
     * Ex: 8686
     */
    val uid: Int,

    /**
     *  Ex: '61'
     */
    val aqi: String = "",

    /**
     *
     * Ex: 'City Railway Station, Bangalore, India'
     */
    val stationName: String = "",

    val stationLatitude: Double,

    val stationLongitude: Double,

    /**
     *
     * Ex: 'IN'
     */
    val stationCountry: String = ""
)
