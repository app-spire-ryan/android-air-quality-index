package com.hireme.android.library.core.networking.api.data

import kotlinx.serialization.Serializable

@Serializable
internal data class AqiRootResponse(

    val status: String,

    val data: AqiResponse
)