package com.hireme.android.library.core.networking.api.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AqiTime(

    /**
     * This is a human-readable string representing the timestamp
     *
     * Ex: '2024-10-15 13:00:00'
     */
    @SerialName("s")
    val timestamp: String,

    /**
     * This is the time zone offset from UTC.
     *
     * Ex: '+08:00'
     */
    @SerialName("tz")
    val timezone: String,

    /**
     * Unix timestamp in seconds.
     *
     * Ex: '1728997200'
     */
    @SerialName("v")
    val timestampSeconds: Long,

    /**
     * This represents the ISO formatted timestamp.
     *
     * Ex: '2024-10-15T13:00:00+08:00'
     */
    @SerialName("iso")
    val timestampIso: String

)
