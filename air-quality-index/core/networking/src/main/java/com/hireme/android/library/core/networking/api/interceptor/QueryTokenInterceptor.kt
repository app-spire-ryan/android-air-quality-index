package com.hireme.android.library.core.networking.api.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * [Interceptor] responsible for appending the token required on every API request.
 *
 * @param token [String] required to make requests.
 */
internal class QueryTokenInterceptor(private val token: String): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        // Get the original request
        val originalRequest: Request = chain.request()

        // Get the original HttpUrl
        val originalHttpUrl: HttpUrl = originalRequest.url

        // Add the token as a query parameter to the original URL
        val urlWithToken: HttpUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("token", token)
            .build()

        // Create a new request with the updated URL
        val requestWithToken: Request = originalRequest.newBuilder()
            .url(urlWithToken)
            .build()

        // Proceed with the new request
        return chain.proceed(requestWithToken)
    }
}