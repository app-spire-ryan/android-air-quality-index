package com.hireme.android.library.core.networking

import com.hireme.android.library.core.networking.api.data.extensions.toAirQualityIndex
import com.hireme.android.library.core.networking.api.data.extensions.toAirQualityIndexSearchResult
import com.hireme.android.library.core.aqi.data.AirQualityIndex
import com.hireme.android.library.core.aqi.data.AirQualityIndexSearchResult
import com.hireme.android.library.core.networking.api.AqiTransceiver
import com.hireme.android.library.core.networking.data.IoError
import com.hireme.android.library.core.networking.data.IoResponse
import com.hireme.android.library.core.networking.extensions.isStatusError
import com.hireme.android.library.core.networking.extensions.isStatusOk
import com.hireme.android.library.core.networking.extensions.toException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Repository responsible for handling AQI related logic.
 */
class AqiRepository(): AqiRepo {

    private companion object {

        private const val TAG = "AqiRepository"
    }

    private val aqiTransceiver: AqiTransceiver by lazy { AqiTransceiver() }

    private fun log(message: String): LogRepo {

        return LogRepository.log(message, TAG)
    }

    @Throws(IllegalStateException::class)
    override suspend fun getAqi(lat: Double, long: Double): Flow<IoResponse<AirQualityIndex>> = flow {

        aqiTransceiver.fetchAqiLatLong(lat, long)
            .catch { e ->

                LogRepository.logError("getAqi(lat: $lat, long: $long) failed with exception: ${e.message}", TAG, e.toException())
                // TODO: Update UI

            }.collect { apiResponse ->

                log("getAqi() apiResponse.body ${apiResponse.body()}")
                log("getAqi() apiResponse.errorBody: ${apiResponse.errorBody()}")
                log("getAqi() apiResponse.message: ${apiResponse.message()}")
                log("getAqi() apiResponse.code: ${apiResponse.code()}")
                log("getAqi() apiResponse.isSuccessful: ${apiResponse.isSuccessful}")

            when {

                apiResponse.body()?.status?.isStatusOk() == true -> {

                    val aqi = apiResponse.body()?.data?.toAirQualityIndex()
                    emit(IoResponse(true to aqi))

                }
                apiResponse.body()?.status?.isStatusError() == true -> {

                    LogRepository.logError("getAqi(lat: $lat, long: $long) failed with an error status: ${apiResponse.body()?.status} | data: ${apiResponse.body()?.data}", TAG)
                    emit(IoResponse(error = IoError("", Exception(""))))
                }
                else -> throw IllegalStateException("$TAG.TAGS: Received an undefined status code of ${apiResponse.body()?.status}")
            }
        }
    }

    @Throws(IllegalStateException::class)
    override suspend fun searchKeyword(keyword: String): Flow<IoResponse<List<AirQualityIndexSearchResult>>> = flow {

        aqiTransceiver.fetchAqiSearch(keyword)
            .catch { e ->

                LogRepository.logError("searchKeyword(keyword: $keyword) failed with exception: ${e.message}", TAG, e.toException())
                // TODO: Update UI

            }.collect { apiResponse ->

                log("searchKeyword() apiResponse.body ${apiResponse.body()}")
                log("searchKeyword() apiResponse.errorBody: ${apiResponse.errorBody()}")
                log("searchKeyword() apiResponse.message: ${apiResponse.message()}")
                log("searchKeyword() apiResponse.code: ${apiResponse.code()}")
                log("searchKeyword() apiResponse.isSuccessful: ${apiResponse.isSuccessful}")

                when {

                    apiResponse.body()?.status?.isStatusOk() == true -> {

                        val results = apiResponse.body()?.data?.map { it.toAirQualityIndexSearchResult() }
                        emit(IoResponse(true to results))

                    }
                    apiResponse.body()?.status?.isStatusError() == true -> {

                        LogRepository.logError("searchKeyword(keyword: $keyword) failed with an error status: ${apiResponse.body()?.status} | data: ${apiResponse.body()?.data}", TAG)
                        emit(IoResponse(error = IoError("", Exception(""))))
                    }

                    else -> throw IllegalStateException("$TAG.TAGS: Received an undefined status code of ${apiResponse.body()?.status}")
                }
            }
    }
}