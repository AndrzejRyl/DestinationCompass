package com.fleenmobile.destinationcompass.util.orientation

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class OrientationDataProviderImpl(
        private val sensorManager: SensorManager,
        private val rotationSensor: Sensor
) : OrientationDataProvider, SensorEventListener {

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private var orientationSubject = PublishSubject.create<Float>()

    override fun setup() {
        sensorManager.registerListener(
                this,
                rotationSensor,
                SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun clear() {
        sensorManager.unregisterListener(this)
    }

    override fun orientation(): Observable<Float> = orientationSubject.throttleFirst(500, TimeUnit.MILLISECONDS)

    //region SensorEventListener
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, it.values)
            SensorManager.remapCoordinateSystem(rotationMatrix,
                    SensorManager.AXIS_X, SensorManager.AXIS_Y,
                    rotationMatrix)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val azimuth = Math.toDegrees(orientationAngles[0].toDouble())
            postOrientation(azimuth)
        }
    }
    //endregion

    private fun postOrientation(azimuth: Double) {
        orientationSubject.onNext(azimuth.toFloat())
    }
}