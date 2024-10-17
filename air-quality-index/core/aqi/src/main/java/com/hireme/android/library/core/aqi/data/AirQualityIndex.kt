package com.hireme.android.library.core.aqi.data

/**
 * Represents AQI information used for the Air Quality Index feature.
 */
data class AirQualityIndex(

    val aqi: Int,

    val stationId: Int,

    val dominantPollutant: String,

    // City

    val latitude: Double,

    val longitude: Double,

    val name: String,

    val location: String,

    // Indoor AQI - Pollutants

    /**
     * 'co'
     */
    val carbonMonoxide: Double,

    /**
     * 'no2'
     */
    val nitrogenDioxide: Double,

    /**
     * 'o3'
     */
    val ozone: Double,

    /**
     * 'pm10'
     */
    val particulateMatter10: Double,

    /**
     * 'pm25'
     */
    val particulateMatter2Point5: Double,

    /**
     * 'so2'
     */
    val sulfurDioxide: Double,

    // Indoor AQI - Environmental Factors

    val humidity: Double,

    val pressure: Double,

    val temperature: Double,

    val windSpeed: Double,

    // Time

    val timestamp: String,

    val timezone: String,

    // Forecast

    val forecastOzone: List<ForecastReading>,

    val forecastParticulateMatter10: List<ForecastReading>,

    val forecastParticulateMatter2Point5: List<ForecastReading>,

    val forecastUvi: List<ForecastReading>
)
