package com.fleenmobile.destinationcompass.util.injection.module

import android.content.Context
import com.fleenmobile.destinationcompass.util.location.LocationDataProvider
import com.fleenmobile.destinationcompass.util.location.LocationDataProviderImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import dagger.Module
import dagger.Provides

@Module
class LocationDataProviderModule {

    @Provides
    fun fusedLocationProviderClient(context: Context): FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

    @Provides
    fun settingsClient(context: Context): SettingsClient =
            LocationServices.getSettingsClient(context)

    @Provides
    fun locationDataProvider(
            fusedLocationProviderClient: FusedLocationProviderClient,
            settingsClient: SettingsClient
    ): LocationDataProvider = LocationDataProviderImpl(fusedLocationProviderClient, settingsClient)
}