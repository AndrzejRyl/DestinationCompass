package com.fleenmobile.destinationcompass.util.location

import android.location.Location
import io.reactivex.Observable

interface LocationDataProvider {

    fun setup()
    fun clear()
    fun location(): Observable<Location>
}