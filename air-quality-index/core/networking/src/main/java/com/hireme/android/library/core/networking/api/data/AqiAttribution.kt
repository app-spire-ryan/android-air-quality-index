package com.hireme.android.library.core.networking.api.data

import kotlinx.serialization.Serializable

@Serializable
internal data class AqiAttribution(

    /**
     *
     * Ex: 'World Air Quality Index Project'
     */
    val name: String,

    /**
     *
     * Ex: 'https://waqi.info/'
     */
    val url: String
)