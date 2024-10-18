package com.hireme.android.library.core.location

import android.content.Context
import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepo {

    suspend fun getLocation(context: Context): Flow<Location?>
}