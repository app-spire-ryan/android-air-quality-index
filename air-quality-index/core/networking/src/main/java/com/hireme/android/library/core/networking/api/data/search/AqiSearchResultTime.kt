package com.hireme.android.library.core.networking.api.data.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AqiSearchResultTime(

    /**
     * Ex: '+05:30'
     */
    @SerialName("tz")
    val timezone: String,

    /**
     * Ex: '2024-10-16 04:00:00'
     */
    @SerialName("stime")
    val timestamp: String,

    /**
     * Ex: '1729031400'
     */
    @SerialName("vtime")
    val timestampSeconds: Long

)
