package com.fleenmobile.destinationcompass.util.rotation

import com.fleenmobile.destinationcompass.feature.compass.presentation.CompassData
import com.fleenmobile.destinationcompass.feature.compass.view.Destination

interface RotationHelper {
    fun calculateRotation(compassData: CompassData, destination: Destination): Float
}