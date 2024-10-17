package com.hireme.android.library.core.networking.api.data.search

import kotlinx.serialization.Serializable

@Serializable
internal data class AqiSearchResultStation(

    /**
     * Ex: 'City Railway Station, Bangalore, India'
     */
    val name: String,

    /**
     * Ex: '[ 12.9756843, 7.5660749]'
     */
    val geo: List<Double>,

    /**
     * Ex: 'india/bangalore/city-railway-station'
     */
    val url: String = "",

    /**
     * Ex: 'IN'
     */
    val country: String = ""
)
