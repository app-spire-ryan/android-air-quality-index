package com.hireme.android.airqualityindex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hireme.android.airqualityindex.ui.theme.AirQualityIndexTheme
import com.hireme.android.library.core.networking.LogRepository

class MainActivity : ComponentActivity() {

    private val vm: AqiViewModel by viewModels()

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

                        Greeting(name = "Android"
                            , modifier = Modifier.padding(innerPadding))

                        Button(modifier = Modifier.padding(innerPadding), onClick = {

                            LogRepository.log("onCreate() button clicked", TAG)

                        }) {

                            Text("AQI")
                        }
                    }
                }
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