package com.hireme.android.library.core.networking.api

import com.hireme.android.library.core.networking.api.data.AqiRootResponse
import com.hireme.android.library.core.networking.api.data.search.AqiSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

/**
 * API Interface responsible for Air Quality Index related features.
 */
internal interface AirQualityIndexApi {

    companion object {

        private const val PATH_CITY = "city"
        private const val PATH_LAT = "lat"
        private const val PATH_LONG = "long"

        private const val QUERY_KEYWORD = "keyword"
    }

    // MARK: /feed calls

    /**
     * This API can be used to get for the real-time Air Quality index for a given station.
     *
     * endpoint: /feed/:city/?token=:token
     * @return [AqiRootResponse] wrapped in [Response]
     */
    @GET("feed/{city}")
    suspend fun fetchAqiCity(@Path(PATH_CITY) city: String): Response<AqiRootResponse>

    /**
     * This API can be used to search for the nearest station from a given latitude/longitude.
     *
     * endpoint: /feed/geo::lat;:lng/?token=:token
     *
     * @param lat
     * @param long
     * @return [AqiRootResponse] wrapped in [Response]
     */
    @GET("feed/geo:{lat};{long}/")
    suspend fun fetchAqiLatLong(@Path(PATH_LAT) lat: Double, @Path(PATH_LONG) long: Double): Response<AqiRootResponse>

    // MARK: /search calls

    /**
     * This API can be used to search stations by name.
     *
     * endpoint: /search/?keyword=:keyword&token=:token
     *
     * @param keyword [String]
     * @return [AqiSearchResponse] wrapped in [Response]
     */
    @GET("search/")
    suspend fun fetchAqiKeyword(@Query(QUERY_KEYWORD) keyword: String): Response<AqiSearchResponse>
}