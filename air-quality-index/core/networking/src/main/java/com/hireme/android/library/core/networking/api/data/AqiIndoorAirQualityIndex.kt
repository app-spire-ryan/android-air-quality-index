package com.hireme.android.library.core.networking.api.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AqiIndoorAirQualityIndex(

    /**
     * Carbon monoxide
     *
     * symbol: 'co'
     */
    @SerialName("co")
    val carbonMonoxide: AqiValue? = null,

    /**
     * Humidity
     *
     * symbol: 'h'
     */
    @SerialName("h")
    val humidity: AqiValue? = null,

    /**
     * Nitrogen dioxide
     *
     * symbol: 'no2'
     */
    @SerialName("no2")
    val nitrogenDioxide: AqiValue? = null,

    /**
     * Ozone
     *
     * symbol: 'o3'
     */
    @SerialName("o3")
    val ozone: AqiValue? = null,

    /**
     * Pressure
     *
     * symbol: 'p'
     */
    @SerialName("p")
    val pressure: AqiValue? = null,

    /**
     * Particulate Matter ≤10 µm
     *
     * symbol: 'pm10'
     */
    @SerialName("pm10")
    val particulateMatter10: AqiValue? = null,

    /**
     * Particulate Matter ≤2.5 µm
     *
     * symbol: 'pm25'
     */
    @SerialName("pm25")
    val particulateMatter2Point5: AqiValue? = null,

    /**
     * Sulfur Dioxide
     *
     * symbol: 'so2'
     */
    @SerialName("so2")
    val sulfurDioxide: AqiValue? = null,

    /**
     * Temperature
     *
     * symbol: 't'
     */
    @SerialName("t")
    val temperature: AqiValue? = null,

    /**
     * Wind Speed
     *
     * symbol: 'w'
     */
    @SerialName("w")
    val windSpeed: AqiValue? = null
)