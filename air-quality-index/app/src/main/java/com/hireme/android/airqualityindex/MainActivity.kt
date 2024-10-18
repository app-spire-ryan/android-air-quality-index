package com.hireme.android.airqualityindex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.hireme.android.airqualityindex.ui.theme.AirQualityIndexTheme
import com.hireme.android.library.core.android.LogRepository
import com.hireme.android.library.core.aqi.data.ForecastReading

@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {

    private val vm: AqiViewModel by viewModels { AqiViewModel.factory(this.application) }

    val locationPermissionRequest = registerForActivityResult(ActivityResultContracts
        .RequestPermission()) { didGrant ->

        vm.onLocationGranted(didGrant)

        if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

            // Show Educational UI
            LogRepository.log("onCreate() should show rationale", TAG)
            vm.onShowLocationRationale()
        }
    }

    fun requestPermLocation() {

        LogRepository.log("requestPermLocation()", TAG)

        locationPermissionRequest.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private companion object {

        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            AirQualityIndexTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
                    ) {

                        val focusManager = LocalFocusManager.current

                        val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        val showRationale by vm.showPermRationale.collectAsState()
                        val dismissedRationale by vm.dismissedRationale.collectAsState()

                        // General Info
                        val coordinates by vm.coordinates.collectAsState()
                        val cityName by vm.selectedCityName.collectAsState()
                        val station by vm.selectedStation.collectAsState()

                        // AQI & Forecast
                        val currentAqi by vm.currentAqi.collectAsState()
                        val forecastYesterday by vm.forecastBefore.collectAsState()
                        val forecastToday by vm.forecastToday.collectAsState()
                        val forecastTomorrow by vm.forecastTomorrow.collectAsState()

                        // Search
                        var searchQuery by remember { mutableStateOf("") }
                        val searchError by vm.searchError.collectAsState()

                        Spacer(modifier = Modifier.height(32.dp))

                        // Show latitude
                        Text(text = "Lat: ${String.format("%.2f", coordinates.first)}",
                            fontSize = 18.sp,
                            color = Color.Magenta
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Show longitude
                        Text(text = "Long: ${String.format("%.2f", coordinates.second)}",
                            fontSize = 18.sp,
                            color = Color.Magenta
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Show city
                        Text(text = "City: $cityName",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Show city
                        Text(text = "Station: $station",
                            fontSize = 18.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Search Input and Button
                        Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { if (searchError.isNotEmpty()) Text("Unable to find city with keyword") else Text("Enter city name") },
                                modifier = Modifier.weight(1f)
                                    .padding(end = 8.dp),
                                isError = searchError.isNotEmpty()
                            )

                            Button(onClick = {

                                vm.onSearch(searchQuery.trim())
                                focusManager.clearFocus()

                            }) {
                                Text("Search")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (!forecastToday?.day.isNullOrEmpty()) {

                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                Text(text = "Current AQI: $currentAqi", color = getAqiColor(currentAqi.toInt()))
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("3 Day Forecast", modifier = Modifier.align(Alignment.CenterHorizontally))

                            Spacer(modifier = Modifier.height(8.dp))

                            // Display forecast with the middle item (present day) centered
                            LazyColumn(modifier = Modifier.fillMaxSize()
                                .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                items(3) { index ->

                                    when(index) {

                                        0 -> {
                                            if (forecastYesterday != null) AQIForecastCard(forecastYesterday)
                                        }
                                        1 -> {
                                            AQIForecastCard(forecastToday)
                                        }
                                        2 -> {
                                            if (forecastTomorrow != null) AQIForecastCard(forecastTomorrow)
                                        }
                                    }
                                }
                            }
                        }

                        // Handle permissions
                        if (isLocationCoarseGranted(this@MainActivity)){

                            LogRepository.log("onCreate() permission granted", TAG)
                            vm.onLocationGranted(true)

                        } else {// Denied

                            LogRepository.log("onCreate() permission denied", TAG)

                            // Add Rationale Dialog
                            if (showRationale) {

                                PermissionRationaleDialog({// onDismiss

                                    LogRepository.log("onDismiss()", TAG)
                                    vm.onLocationRationaleDismissed()

                                }, {// onRequestPermission

                                    LogRepository.log("onRequestPermissions()", TAG)
                                    requestPermLocation()
                                })

                            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                                // Show Educational UI
                                LogRepository.log("onCreate() should show rationale", TAG)
                                    .log("onCreate() dismissedRationale: $dismissedRationale", TAG)

                                if (dismissedRationale == null || dismissedRationale == false) {

                                    vm.onShowLocationRationale()
                                }

                            } else {

                                LogRepository.log("onCreate() should NOT show rationale", TAG)
                                requestPermLocation()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getAqiColor(value: Int): Color {

    val goodColor = colorResource(id = R.color.aqi_good)
    val moderateColor = colorResource(id = R.color.aqi_moderate)
    val unhealthySensitiveColor = colorResource(id = R.color.aqi_unhealthy_sensitive)
    val unhealthyColor = colorResource(id = R.color.aqi_unhealthy)
    val veryUnhealthyColor = colorResource(id = R.color.aqi_very_unhealthy)
    val hazardColor = colorResource(id = R.color.aqi_hazard)

    return when (value) {
        in 0..50 -> goodColor
        in 51..100 -> moderateColor
        in 101..150 -> unhealthySensitiveColor // TODO: Update to Orange!
        in 151..200 -> unhealthyColor
        in 201..300 -> veryUnhealthyColor
        in 300 .. Int.MAX_VALUE -> hazardColor
        else -> Color.Magenta
    }
}

@Composable
fun PermissionRationaleDialog(onDismiss: () -> Unit, onRequestPermission: () -> Unit) {

    AlertDialog(onDismissRequest = onDismiss,
        title = { Text("Location Permission Required") },
        text = {
            Text("We need access to your location to show the Air Quality Index (AQI) for your area. Please grant the permission to continue.")
        },
        confirmButton = {
            TextButton(onClick = {
                onRequestPermission()
                onDismiss()
            }) {
                Text("Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AQIForecastCard(forecast: ForecastReading?) {

    Card(modifier = Modifier.fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(6.dp))
        , elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        , shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            forecast?.let { cast ->

                Text(text = cast.day, fontSize = 20.sp, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Average AQI: ${cast.average}",
                    fontSize = 22.sp,
                    color = getAqiColor(cast.average)
                )
                Text(text = "Max AQI: ${cast.max}",
                    fontSize = 24.sp,
                    color = getAqiColor(cast.max)
                )
                Text(text = "Min AQI: ${cast.min}",
                    fontSize = 24.sp,
                    color = getAqiColor(cast.min)
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    AirQualityIndexTheme {

        Greeting("Android")
    }
}