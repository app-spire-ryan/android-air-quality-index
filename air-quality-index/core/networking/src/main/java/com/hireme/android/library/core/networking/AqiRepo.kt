package com.hireme.android.library.core.networking

import com.hireme.android.library.core.aqi.data.AirQualityIndex
import com.hireme.android.library.core.aqi.data.AirQualityIndexSearchResult
import com.hireme.android.library.core.networking.data.IoResponse
import kotlinx.coroutines.flow.Flow

interface AqiRepo {

    suspend fun getAqi(lat: Double, long: Double): Flow<IoResponse<AirQualityIndex>>

    suspend fun searchKeyword(keyword: String): Flow<IoResponse<List<AirQualityIndexSearchResult>>>
}