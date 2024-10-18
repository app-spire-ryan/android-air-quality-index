package com.hireme.android.airqualityindex

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isLocationCoarseGranted(context: Context): Boolean = ContextCompat
    .checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED