package com.hireme.android.library.core.networking.api.data

import com.hireme.android.library.core.networking.api.data.forecast.AqiForecast
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AqiResponse(

    /**
     * Ex: 104
     */
    val aqi: Int,

    /**
     * Ex: 1437
     */
    @SerialName("idx")
    val stationId: Int,

    val attributions: List<AqiAttribution>,

    val city: AqiCity,

    /**
     * Ex: 'pm25'
     */
    @SerialName("dominentpol")
    val dominantPollutant: String,

    /**
     * Ex:
     */
    @SerialName("iaqi")
    val indoorAirQualityIndex: AqiIndoorAirQualityIndex,

    val time: AqiTime,

    val forecast: AqiForecast
)