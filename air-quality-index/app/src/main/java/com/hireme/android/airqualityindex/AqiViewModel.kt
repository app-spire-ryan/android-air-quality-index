package com.hireme.android.airqualityindex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hireme.android.library.core.networking.AqiRepository
import com.hireme.android.library.core.android.LogRepository
import com.hireme.android.library.core.aqi.data.AirQualityIndex
import com.hireme.android.library.core.aqi.data.AirQualityIndexSearchResult
import com.hireme.android.library.core.location.LocationRepo
import com.hireme.android.library.core.location.LocationRepository
import com.hireme.android.library.core.android.extensions.toException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AqiViewModel(app: Application, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): AndroidViewModel(app) {

    private val repo: AqiRepository by lazy { AqiRepository() }
    private val locationRepo: LocationRepo by lazy { LocationRepository() }

    private val _locationGranted: MutableLiveData<Boolean> = MutableLiveData()
    val locationGranted: LiveData<Boolean> = _locationGranted

    private val _showPermRationale: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showPermRationale: StateFlow<Boolean> = _showPermRationale

    private val _permRationaleDismissed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val permRationaleDismissed: StateFlow<Boolean> = _permRationaleDismissed

    private val _selectedCity: MutableLiveData<String> = MutableLiveData()
    val selectedCity: LiveData<String> = _selectedCity

    private val _selectedStation: MutableLiveData<String> = MutableLiveData()
    val selectedStation: LiveData<String> = _selectedStation

    private val _airQualityIndex: MutableLiveData<AirQualityIndex> = MutableLiveData()
    val airQualityIndex: LiveData<AirQualityIndex> = _airQualityIndex

    companion object {

        private const val TAG = "AqiViewModel"

        fun factory(app: Application) = object : ViewModelProvider.AndroidViewModelFactory(app) {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                if (modelClass.isAssignableFrom(AqiViewModel::class.java)) {
                    return AqiViewModel(app) as T
                } else throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    init {
        log("init()")
    }

    private fun log(message: String) {

        LogRepository.log(message, TAG)
    }

    /**
     * Should be called when the location permission has been granted.
     *
     * @param didGrant Whether or not the permission has been granted.
     */
    fun onLocationGranted(didGrant: Boolean) {

        viewModelScope.launch {

            log("onLocationGranted() didGrant: $didGrant")

            // Update grant status
            _locationGranted.value = didGrant

            withContext(ioDispatcher) {

                if (didGrant) {

                    // Get the location
                    locationRepo.getLocation(getApplication()).catch { e ->

                        LogRepository.logError("onLocationGranted() message: ${e.message}", TAG, e.toException())

                    }.collect { location ->

                        log("onLocationGranted() location: ${location?.latitude} | longitude: ${location?.longitude}")
                        location?.let {

                            // Read AQI based on location
                            getAqi(it.latitude, it.longitude)
                        }
                    }
                }
            }
        }
    }

    /**
     * Should be called when the permission rationale needs to be shown.
     */
    fun onShowLocationRationale() {

        log("onShowLocationRationale()")
        _showPermRationale.value = true
    }

    /**
     * Should be called when the permission rationale needs to be dismissed.
     */
    fun onLocationRationaleDismissed() {

        log("onLocationRationaleDismissed()")
        _showPermRationale.value = false
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