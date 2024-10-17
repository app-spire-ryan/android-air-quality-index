package com.hireme.android.library.core.networking

import android.util.Log

object LogRepository: LogRepo {

    override fun log(message: String, tag: String): LogRepo {

        Log.d(tag, ".TAGS: $message")
        return this
    }

    override fun logError(message: String, tag: String, exception: Exception?): LogRepo {

        Log.e(tag, ".TAGS: message: $message", exception)
        return this
    }
}