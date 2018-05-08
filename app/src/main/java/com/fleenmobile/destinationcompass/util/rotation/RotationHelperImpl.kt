package com.fleenmobile.destinationcompass.util.rotation

import android.hardware.GeomagneticField
import android.location.Location
import com.fleenmobile.destinationcompass.feature.compass.presentation.CompassData
import com.fleenmobile.destinationcompass.feature.compass.view.Destination

class RotationHelperImpl : RotationHelper {

    override fun calculateRotation(compassData: CompassData, destination: Destination): Float {
        var azimuth = compassData.orientation
        val currentLocation = compassData.location
        val destinationLocation = Location(currentLocation.provider).apply {
            this.latitude = destination.latitude ?: 0.0
            this.longitude = destination.longitude ?: 0.0
        }

        val geoField = GeomagneticField(
                currentLocation.latitude.toFloat(),
                currentLocation.longitude.toFloat(),
                currentLocation.altitude.toFloat(),
                System.currentTimeMillis())

        azimuth -= geoField.declination

        var bearTo = currentLocation.bearingTo(destinationLocation)

        // If the bearTo is smaller than 0, add 360 to get the rotation clockwise.
        if (bearTo < 0) {
            bearTo += 360
        }

        //This is where we choose to point it
        var direction = bearTo - azimuth

        // If the direction is smaller than 0, add 360 to get the rotation clockwise.
        if (direction < 0) {
            direction += 360
        }

        return direction
    }
}