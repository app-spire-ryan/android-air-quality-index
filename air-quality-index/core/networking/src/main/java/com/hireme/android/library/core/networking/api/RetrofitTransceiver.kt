package com.hireme.android.library.core.networking.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit

abstract class RetrofitTransceiver {

    protected abstract val client: OkHttpClient

    protected abstract val retrofit: Retrofit
}