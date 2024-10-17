package com.hireme.android.library.core.networking.api

import com.hireme.android.library.core.networking.BuildConfig
import com.hireme.android.library.core.networking.api.data.AqiRootResponse
import com.hireme.android.library.core.networking.api.data.search.AqiSearchResponse
import com.hireme.android.library.core.networking.api.interceptor.QueryTokenInterceptor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Transceiver responsible to making requests to AQI API.
 */
internal class AqiTransceiver: RetrofitTransceiver() {

    private companion object {

        private const val TAG = "Transceiver"

        private const val TIMEOUT_DEFAULT = 30L
        private const val BASE_URL = "https://api.waqi.info/"
        private const val CONTENT_TYPE = "application/json; charset=UTF8"
    }

    private val queryTokenInterceptor: QueryTokenInterceptor by lazy { QueryTokenInterceptor(BuildConfig.AQI_TOKEN) }

    override val client: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(TIMEOUT_DEFAULT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_DEFAULT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_DEFAULT, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT_DEFAULT, TimeUnit.SECONDS)
        .addInterceptor(queryTokenInterceptor)
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .client(client)
        .build()

    private fun buildApi(): AirQualityIndexApi {

        return retrofit.create(AirQualityIndexApi::class.java)
    }

    /**
     * Performs a block of logic after building the [AirQualityIndexApi].
     *
     * @param block to execute after building [AirQualityIndexApi]
     */
    private inline fun <T> request(block: (api: AirQualityIndexApi) -> T) {

        val api = buildApi()
        block.invoke(api)
    }

    /**
     * Fetches the nearest station from a given latitude/longitude.
     *
     * @param lat [Double]
     * @param long [Double]
     * @return [Flow] of type [Response] of type [AqiSearchResponse]
     */
    suspend fun fetchAqiLatLong(lat: Double, long: Double): Flow<Response<AqiRootResponse>> = flow {

        request { api ->

            emit(api.fetchAqiLatLong(lat, long))
        }
    }

    /**
     * Fetches search results for stations by name.
     *
     * @param keyword [String]
     * @return [Flow] of type [Response] of type [AqiSearchResponse]
     */
    suspend fun fetchAqiSearch(keyword: String): Flow<Response<AqiSearchResponse>> = flow {

        request { api ->

            emit(api.fetchAqiKeyword(keyword))
        }
    }
}