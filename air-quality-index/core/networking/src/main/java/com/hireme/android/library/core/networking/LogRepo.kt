package com.hireme.android.library.core.networking

interface LogRepo {

    fun log(message: String, tag: String): LogRepo

    fun logError(message: String, tag: String, exception: Exception? = null): LogRepo
}