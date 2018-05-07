package com.fleenmobile.destinationcompass.util.injection.module

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import com.fleenmobile.destinationcompass.util.orientation.OrientationDataProvider
import com.fleenmobile.destinationcompass.util.orientation.OrientationDataProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class OrientationDataProviderModule {

    @Provides
    fun sensorManager(context: Context): SensorManager =
            context.getSystemService(SENSOR_SERVICE) as SensorManager

    @Provides
    @Named("accelerometer")
    fun accelerometer(sensorManager: SensorManager): Sensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    @Provides
    @Named("magnetometer")
    fun magnetometer(sensorManager: SensorManager): Sensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    @Provides
    fun orientationDataProvider(
            sensorManager: SensorManager,
            @Named("accelerometer") accelerometer: Sensor,
            @Named("magnetometer") magnetometer: Sensor
    ): OrientationDataProvider = OrientationDataProviderImpl(sensorManager, accelerometer, magnetometer)
}