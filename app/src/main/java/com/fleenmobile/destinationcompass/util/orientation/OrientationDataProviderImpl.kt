package com.fleenmobile.destinationcompass.util.orientation

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class OrientationDataProviderImpl(
        private val sensorManager: SensorManager,
        private val accelerometer: Sensor,
        private val magnetometer: Sensor
) : OrientationDataProvider, SensorEventListener {

    private var accelerometerReading = FloatArray(3)
    private var magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var orientationSubject = BehaviorSubject.create<Float>()

    override fun setup() {
        registerListener(accelerometer)
        registerListener(magnetometer)
    }

    private fun registerListener(sensor: Sensor) =
            sensorManager.registerListener(
                    this,
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
            )

    override fun clear() {
        sensorManager.unregisterListener(this)
    }

    override fun orientation(): Observable<Float> = orientationSubject

    //region SensorEventListener
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> accelerometerReading = event.values
            Sensor.TYPE_MAGNETIC_FIELD -> magnetometerReading = event.values
        }
        postOrientation()
    }
    //endregion

    private fun postOrientation() {
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading)

        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        orientationSubject.onNext(orientationAngles[0])
    }
}