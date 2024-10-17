package com.hireme.android.library.core.networking.api.data.extensions

import com.hireme.android.library.core.networking.api.data.search.AqiSearchResult
import com.hireme.android.library.core.aqi.data.AirQualityIndexSearchResult

/**
 * Converts [AqiSearchResult] to [AirQualityIndexSearchResult].
 *
 * @return [AirQualityIndexSearchResult]
 */
internal fun AqiSearchResult.toAirQualityIndexSearchResult(): AirQualityIndexSearchResult
    = AirQualityIndexSearchResult(uid, aqi, station.name, station.geo[0], station.geo[0], station.country)