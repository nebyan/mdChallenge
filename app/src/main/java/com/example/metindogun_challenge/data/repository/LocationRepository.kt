package com.example.metindogun_challenge.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface LocationRepository{
    fun getLocationUpdates(): Flow<Location>
}

@SuppressLint("MissingPermission")
@ExperimentalCoroutinesApi
class LocationRepositoryImpl @Inject constructor(@ApplicationContext var context: Context): LocationRepository {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.create().apply {
        interval = 10_000
        fastestInterval = 5_000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    var lastLocation: Location? = null

    override fun getLocationUpdates(): Flow<Location> = callbackFlow{

        val callback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (lastLocation == null || lastLocation!!.distanceTo(locationResult.lastLocation)>100){
                    lastLocation = locationResult.lastLocation
                    trySend(locationResult.lastLocation)
                }
                Log.i(TAG, "locationUpdated: ${locationResult.lastLocation}")
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, callback, Looper.myLooper()!!)

        awaitClose{ fusedLocationClient.removeLocationUpdates(callback)}
    }

    companion object {
        const val TAG = "LocationProvider"
    }

}