package com.hireme.android.library.core.networking.api.data

import kotlinx.serialization.Serializable

@Serializable
internal data class AqiCity(

    val geo: List<Double>,

    val name: String = "",

    val url: String = "",

    val location: String = ""
)