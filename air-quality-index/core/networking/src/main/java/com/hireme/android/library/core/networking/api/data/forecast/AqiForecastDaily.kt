package com.hireme.android.library.core.networking.api.data.forecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AqiForecastDaily(

    @SerialName("o3")
    val ozone: List<AqiForecastDayReading>,

    @SerialName("pm10")
    val particulateMatter10: List<AqiForecastDayReading>,

    @SerialName("pm25")
    val particulateMatter2Point5: List<AqiForecastDayReading>,

    @SerialName("uvi")
    val ultravioletIndex: List<AqiForecastDayReading>
)