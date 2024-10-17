package com.hireme.android.library.core.networking.api.data.search

import kotlinx.serialization.Serializable

@Serializable
internal data class AqiSearchResponse(

    val status: String,

    val data: List<AqiSearchResult>
)
