package com.hireme.android.library.core.networking.api.data.forecast

import kotlinx.serialization.Serializable

@Serializable
internal data class AqiForecast(

    val daily: AqiForecastDaily
)