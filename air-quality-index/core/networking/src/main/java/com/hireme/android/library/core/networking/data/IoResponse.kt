package com.hireme.android.library.core.networking.data

/**
 *
 */
data class IoResponse<T>(

    val result: Pair<Boolean, T?> = false to null,

    val error: IoError? = null
)