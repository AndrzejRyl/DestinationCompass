package com.fleenmobile.destinationcompass.util.injection.module

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import com.fleenmobile.destinationcompass.util.orientation.OrientationDataProvider
import com.fleenmobile.destinationcompass.util.orientation.OrientationDataProviderImpl
import dagger.Module
import dagger.Provides

@Module
class OrientationDataProviderModule {

    @Provides
    fun sensorManager(context: Context): SensorManager =
            context.getSystemService(SENSOR_SERVICE) as SensorManager

    @Provides
    fun rotationSensor(sensorManager: SensorManager): Sensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    @Provides
    fun orientationDataProvider(
            sensorManager: SensorManager,
            rotationSensor: Sensor
    ): OrientationDataProvider = OrientationDataProviderImpl(sensorManager, rotationSensor)
}