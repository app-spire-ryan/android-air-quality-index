package com.hireme.android.library.core.networking.api.data.search

import kotlinx.serialization.Serializable

@Serializable
internal data class AqiSearchResult(

    /**
     * Ex: 8686
     */
    val uid: Int,

    /**
     * Ex: '61'
     */
    val aqi: String,

    val time: AqiSearchResultTime,

    val station: AqiSearchResultStation
)
