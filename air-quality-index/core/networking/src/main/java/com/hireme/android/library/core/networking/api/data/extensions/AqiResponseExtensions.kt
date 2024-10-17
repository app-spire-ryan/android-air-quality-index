package com.hireme.android.library.core.networking.api.data.extensions

import com.hireme.android.library.core.networking.api.data.AqiResponse
import com.hireme.android.library.core.networking.api.data.forecast.extensions.toOzoneForecast
import com.hireme.android.library.core.networking.api.data.forecast.extensions.toParticulateMatter10Forecast
import com.hireme.android.library.core.networking.api.data.forecast.extensions.toParticulateMatter2Point5Forecast
import com.hireme.android.library.core.networking.api.data.forecast.extensions.toUviForecast
import com.hireme.android.library.core.aqi.data.AirQualityIndex

/**
 * Converts [AqiResponse] to [AirQualityIndex].
 *
 * @return [AirQualityIndex]
 */
internal fun AqiResponse.toAirQualityIndex(): AirQualityIndex = AirQualityIndex(this.aqi, this.stationId
    , this.dominantPollutant
        // City
    , this.city.geo[0], this.city.geo[1], this.city.name, this.city.location
        // IAQI
    , this.indoorAirQualityIndex.carbonMonoxide?.value ?: -1.0, this.indoorAirQualityIndex.nitrogenDioxide?.value  ?: -1.0
    , this.indoorAirQualityIndex.ozone?.value ?: -1.0, this.indoorAirQualityIndex.particulateMatter10?.value ?: -1.0
    , this.indoorAirQualityIndex.particulateMatter2Point5?.value ?: -1.0, this.indoorAirQualityIndex.sulfurDioxide?.value ?: -1.0
    , this.indoorAirQualityIndex.humidity?.value ?: -1.0, this.indoorAirQualityIndex.pressure?.value ?: -1.0
    , this.indoorAirQualityIndex.temperature?.value ?: -1.0, this.indoorAirQualityIndex.windSpeed?.value ?: -1.0
        // Time
    , this.time.timestamp, this.time.timezone
        // Forecast
    , this.forecast.daily.toOzoneForecast(this.time.timestampToDay())
    , this.forecast.daily.toParticulateMatter10Forecast(this.time.timestampToDay())
    , this.forecast.daily.toParticulateMatter2Point5Forecast(this.time.timestampToDay())
    , this.forecast.daily.toUviForecast(this.time.timestampToDay()))