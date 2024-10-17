package com.hireme.android.airqualityindex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hireme.android.library.core.networking.AqiRepository
import com.hireme.android.library.core.networking.LogRepository
import com.hireme.android.library.core.aqi.data.AirQualityIndex
import com.hireme.android.library.core.aqi.data.AirQualityIndexSearchResult
import com.hireme.android.library.core.networking.extensions.toException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AqiViewModel(app: Application): AndroidViewModel(app) {

    private val repo: AqiRepository by lazy { AqiRepository() }

    private val _coordinates: MutableLiveData<Pair<Long,Long>> = MutableLiveData()
    val coordinates: LiveData<Pair<Long,Long>> = _coordinates

    private val _selectedCity: MutableLiveData<String> = MutableLiveData()
    val selectedCity: LiveData<String> = _selectedCity

    private val _selectedStation: MutableLiveData<String> = MutableLiveData()
    val selectedStation: LiveData<String> = _selectedStation

    private val _airQualityIndex: MutableLiveData<AirQualityIndex> = MutableLiveData()
    val airQualityIndex: LiveData<AirQualityIndex> = _airQualityIndex

    companion object {

        private const val TAG = "AqiViewModel"
    }

    init {
        log("init()")
    }

    private fun log(message: String) {

        LogRepository.log(message, TAG)
    }

   private fun getAqi(lat: Double, long: Double) {

        viewModelScope.launch {

            //repo.getAqi(28.5384, 81.3789)
            //repo.getAqi(28.5, 81.3)
            //repo.getAqi(10.3, 20.7)
            //repo.getAqi(39.1031, 84.5120)
            repo.getAqi(lat, long)
                .catch { e ->

                    LogRepository.logError("getAqi() message: ${e.message}", TAG, e.toException())
                    // TODO: Update UI

                }.collect { ioResponse ->

                    if (ioResponse.result.first) {// Handle aqi

                        val airQualityIndex = ioResponse.result.second
                        log("getAqi() airQualityIndex: $airQualityIndex")
                        // TODO: Update UI

                    } else {// Handle error

                        LogRepository.logError("getAqi() error: ${ioResponse.error}", TAG)
                        // TODO: Update UI
                    }
                }
        }
    }

    fun onSearch(keyword: String) {

        viewModelScope.launch {

            log("onSearch() keyword: $keyword")

            repo.searchKeyword(keyword)
                .catch { e ->

                    LogRepository.logError("onSearch() message: ${e.message}", TAG, e.toException())
                    // TODO: Update UI

                }.collect { ioResponse ->

                    if (ioResponse.result.first) {// Handle aqi

                        val searchResults = ioResponse.result.second
                        log("onSearch() first result: ${searchResults?.firstOrNull()}")

                        // Take first entry
                        val first: AirQualityIndexSearchResult = searchResults?.firstOrNull()
                            ?: throw IllegalStateException("TODO: No search results returned.") // TODO: Update UI with empty search

                        // Now that we have the search result, fetch the AQI data for it
                        getAqi(first.stationLatitude, first.stationLongitude)

                        // TODO: Update UI

                    } else {// Handle error

                        LogRepository.logError("onSearch() error: ${ioResponse.error}", TAG)
                        // TODO: Update UI
                    }
                }
        }
    }
}