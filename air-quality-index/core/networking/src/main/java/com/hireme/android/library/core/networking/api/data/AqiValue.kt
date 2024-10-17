package com.hireme.android.library.core.networking.api.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AqiValue(

    @SerialName("v")
    val value: Double
)