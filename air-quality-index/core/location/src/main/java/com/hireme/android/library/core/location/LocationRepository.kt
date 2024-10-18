package com.hireme.android.library.core.location

import android.Manifest
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hireme.android.library.core.android.LogRepository
import com.hireme.android.library.core.android.extensions.toException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Repository responsible for handling Location related logic.
 */
class LocationRepository: LocationRepo {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private companion object {

        private const val TAG = "LocationRepository"
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    private fun readLastLocation(context: Context): Flow<Location?> = callbackFlow {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            // Got last known location. In some rare situations this can be null.
            LogRepository.log("onLastLocationSuccessListener.readLastLocation() latitude: ${location?.latitude} | longitude: ${location?.longitude}", TAG)
            location?.let {

                trySend(it).onFailure { e ->

                    LogRepository.logError("OnSuccessListener.readLastLocation() trySend onFailure: ${e?.message}", TAG, e?.toException())
                    // TODO: See if we need to handle

                }.onSuccess {

                    LogRepository.log("OnSuccessListener.readLastLocation() trySend onSuccess", TAG)
                }
            }

        }

        awaitClose {
            LogRepository.log("OnSuccessListener.readLastLocation() awaited close", TAG)
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    override suspend fun getLocation(context: Context): Flow<Location?> {

        LogRepository.log("getLocation()", TAG)
        return readLastLocation(context)
    }
}