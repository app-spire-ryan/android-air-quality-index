package com.hireme.android.library.core.networking.api.data.forecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AqiForecastDayReading(

    /**
     * Ex: 1
     */
    @SerialName("avg")
    val average: Int,

    /**
     * Ex: '2024-10-17'
     */
    val day: String,

    /**
     * Ex: 3
     */
    val max: Int,

    /**
     * Ex: 0
     */
    val min: Int
)