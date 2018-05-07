package com.fleenmobile.destinationcompass.util.orientation

import io.reactivex.Observable

interface OrientationDataProvider {

    fun setup()
    fun clear()
    fun orientation(): Observable<Float>
}