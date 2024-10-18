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
import com.hireme.android.library.core.aqi.data.ForecastReading
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

    private val _coordinates: MutableStateFlow<Pair<Double, Double>> = MutableStateFlow(0.0 to 0.0)
    val coordinates: StateFlow<Pair<Double, Double>> = _coordinates

    private val _showPermRationale: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showPermRationale: StateFlow<Boolean> = _showPermRationale

    private val _dismissedRationale: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val dismissedRationale: StateFlow<Boolean?> = _dismissedRationale

    private val _permRationaleDismissed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val permRationaleDismissed: StateFlow<Boolean> = _permRationaleDismissed

    private val _selectedCityName: MutableStateFlow<String> = MutableStateFlow("NA")
    val selectedCityName: StateFlow<String> = _selectedCityName

    private val _selectedCityLocation: MutableStateFlow<String> = MutableStateFlow("NA")
    val selectedCityLocation: StateFlow<String> = _selectedCityLocation

    private val _selectedStation: MutableStateFlow<String> = MutableStateFlow("NA")
    val selectedStation: StateFlow<String> = _selectedStation

    private val _currentAqi: MutableStateFlow<String> = MutableStateFlow("0")
    val currentAqi: StateFlow<String> = _currentAqi

    private val _forecastBefore: MutableStateFlow<ForecastReading?> = MutableStateFlow(ForecastReading())
    val forecastBefore: StateFlow<ForecastReading?> = _forecastBefore

    private val _forecastToday: MutableStateFlow<ForecastReading?> = MutableStateFlow(ForecastReading())
    val forecastToday: StateFlow<ForecastReading?> = _forecastToday

    private val _forecastTomorrow: MutableStateFlow<ForecastReading?> = MutableStateFlow(ForecastReading())
    val forecastTomorrow: StateFlow<ForecastReading?> = _forecastTomorrow


    private val _airQualityIndex: MutableLiveData<AirQualityIndex> = MutableLiveData()
    val airQualityIndex: LiveData<AirQualityIndex> = _airQualityIndex

    private val _searchError: MutableStateFlow<String> = MutableStateFlow("")
    val searchError: StateFlow<String> = _searchError

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

    private fun updateCoordinates(lat: Double, long: Double) {

        _coordinates.value = lat to long
    }

    /**
     * Should be called when the location permission has been granted.
     *
     * @param didGrant Whether or not the permission has been granted.
     */
    fun onLocationGranted(didGrant: Boolean) {

        viewModelScope.launch {

            log("onLocationGranted() didGrant: $didGrant")

            // If location is already granted no need to re-launch location fetching
            if (locationGranted.value == true) return@launch

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

                            // Update the UI with the coordinates
                            updateCoordinates(it.latitude, it.longitude)

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
        _dismissedRationale.value = true
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

                        // Update the coordinates
                        updateCoordinates(airQualityIndex?.latitude ?: 0.0, airQualityIndex?.longitude ?: 0.0)

                        // Update UI
                        _selectedCityName.value = airQualityIndex?.name ?: "NA"
                        _selectedStation.value = airQualityIndex?.stationId.toString()
                        _currentAqi.value = airQualityIndex?.aqi.toString()
                        _forecastBefore.value = airQualityIndex?.forecastOzone?.first()
                        _forecastToday.value = airQualityIndex?.forecastOzone?.getOrNull(1)
                        _forecastTomorrow.value = airQualityIndex?.forecastOzone?.getOrNull(2)

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

            // Clear search error
            _searchError.value = ""

            repo.searchKeyword(keyword)
                .catch { e ->

                    LogRepository.logError("onSearch() message: ${e.message}", TAG, e.toException())
                    // TODO: Update UI

                }.collect { ioResponse ->

                    if (ioResponse.result.first) {// Handle aqi

                        val searchResults = ioResponse.result.second
                        log("onSearch() first result: ${searchResults?.firstOrNull()}")

                        // Take first entry
                        val first: AirQualityIndexSearchResult? = searchResults?.firstOrNull()

                        if (first == null) _searchError.value = "No search results were returned for $keyword"

                        first?.let {

                            // Now that we have the search result, fetch the AQI data for it
                            getAqi(it.stationLatitude, it.stationLongitude)
                        }

                    } else {// Handle error

                        LogRepository.logError("onSearch() error: ${ioResponse.error}", TAG)
                        _searchError.value = "No search results were returned for $keyword"
                    }
                }
        }
    }
}