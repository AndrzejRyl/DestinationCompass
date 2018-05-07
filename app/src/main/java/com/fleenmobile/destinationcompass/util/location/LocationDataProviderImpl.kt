package com.fleenmobile.destinationcompass.util.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class LocationDataProviderImpl(
        private val fusedLocationProviderClient: FusedLocationProviderClient,
        private val settingsClient: SettingsClient
) : LocationDataProvider {

    companion object {
        private const val UPDATE_INTERVAL_IN_MILLISECONDS = 5000L
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000L
    }

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationSettingsRequest: LocationSettingsRequest
    private lateinit var locationCallback: LocationCallback

    private val locationSubject = BehaviorSubject.create<Location>()

    override fun setup() {
        locationRequest = LocationRequest().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationSettingsRequest = LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest)
                .build()

        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener { _ -> requestLocationUpdates() }
                .addOnFailureListener { locationSubject.onError(it) }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                postLocation(locationResult)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
        )
    }

    private fun postLocation(locationResult: LocationResult?) =
            locationResult?.let {
                locationSubject.onNext(it.lastLocation)
            }

    override fun clear() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun location(): Observable<Location> = locationSubject
}