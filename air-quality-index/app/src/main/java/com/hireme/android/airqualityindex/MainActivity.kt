package com.hireme.android.airqualityindex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.hireme.android.airqualityindex.ui.theme.AirQualityIndexTheme
import com.hireme.android.library.core.android.LogRepository

@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {

    private val vm: AqiViewModel by viewModels { AqiViewModel.factory(this.application) }

    val locationPermissionRequest = registerForActivityResult(ActivityResultContracts
        .RequestPermission()) { didGrant ->

        vm.onLocationGranted(didGrant)
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

                    Column(modifier = Modifier.fillMaxSize()
                        , verticalArrangement = Arrangement.Center
                        , horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        val locationPermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        val showRationale by vm.showPermRationale.collectAsState()

                        Greeting(name = "Android", modifier = Modifier.padding(innerPadding))

                        Button(modifier = Modifier.padding(innerPadding), onClick = {

                            LogRepository.log("onCreate() button clicked", TAG)

                            if (isLocationCoarseGranted(this@MainActivity)){

                                LogRepository.log("onCreate() permission granted", TAG)

                                // Granted
                                vm.onLocationGranted(true)

                            } else {// Denied

                                LogRepository.log("onCreate() permission denied", TAG)

                                if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                                    // Show Educational UI
                                    LogRepository.log("onCreate() should show rationale", TAG)
                                    vm.onShowLocationRationale()

                                } else {

                                    LogRepository.log("onCreate() should NOT show rationale", TAG)
                                    requestPermLocation()
                                }
                            }

                        }) {

                            Text("AQI")
                        }

                        // Add Rationale Dialog
                        if (showRationale) PermissionRationaleDialog({// onDismiss

                            LogRepository.log("onDismiss()", TAG)
                            vm.onLocationRationaleDismissed()

                        }, {// onRequestPermission

                            LogRepository.log("onRequestPermissions()", TAG)
                            requestPermLocation()
                        })
                    }
                }
            }
        }
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