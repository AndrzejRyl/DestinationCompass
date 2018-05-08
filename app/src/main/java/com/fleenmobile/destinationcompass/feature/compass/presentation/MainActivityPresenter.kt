package com.fleenmobile.destinationcompass.feature.compass.presentation

import android.hardware.GeomagneticField
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import com.fleenmobile.destinationcompass.feature.compass.view.Destination
import com.fleenmobile.destinationcompass.util.location.LocationDataProvider
import com.fleenmobile.destinationcompass.util.orientation.OrientationDataProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import java.text.DecimalFormat

data class CompassData(val orientation: Float, val location: Location)

class MainActivityPresenter(
        private val view: MainActivityContract.View,
        private val orientationDataProvider: OrientationDataProvider,
        private val locationDataProvider: LocationDataProvider,
        private val compositeDisposable: CompositeDisposable,
        private val locationManager: LocationManager
) : MainActivityContract.Presenter {

    companion object {
        private val DECIMAL_FORMAT = DecimalFormat("0.####")
    }

    private lateinit var compassDataObservable: Observable<CompassData>
    private lateinit var compassDataDisposable: Disposable

    override fun initialize() {
        compassDataObservable = Observable.combineLatest(
                orientationDataProvider.orientation(),
                locationDataProvider.location(),
                BiFunction { orientation, location -> CompassData(orientation, location) }
        )

        forcePrerequisites()
    }

    private fun forcePrerequisites() {
        view.apply {
            if (!permissionsEnabled) showPermissionsRequiredInfo()
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) showGPSEnabledRequiredInfo()
        }
    }

    override fun clear() {
        compositeDisposable.clear()
        orientationDataProvider.clear()
        locationDataProvider.clear()
    }

    override fun changeDestinationClicked() {
        view.showDestinationForm()
        stopUpdatingCompass()
    }

    override fun destinationChosen(destination: Destination) = with(view) {
        val (latitude, longitude) = destination
        showDestination(parseGeoValue(latitude), parseGeoValue(longitude))

        if (longitude == null || latitude == null) {
            showDestinationRequiredInfo()
            stopUpdatingCompass()
        } else {
            startUpdatingCompass(destination)
        }
    }

    private fun startUpdatingCompass(destination: Destination) {
        if (view.isArrowEnabled) return

        view.enableArrow()
        compassDataDisposable = compassDataObservable
                .subscribe(
                        { view.rotateArrow(calculateRotation(it, destination)) },
                        {
                            Log.e(MainActivityPresenter::class.java.name.toUpperCase(), it.message)
                            view.showError("GPS Error")
                        }
                )
        compositeDisposable.add(compassDataDisposable)

        orientationDataProvider.setup()
        locationDataProvider.setup()
    }

    private fun stopUpdatingCompass() {
        if (!view.isArrowEnabled) return

        orientationDataProvider.clear()
        locationDataProvider.clear()
        compositeDisposable.remove(compassDataDisposable)
        view.disableArrow()
    }

    private fun calculateRotation(compassData: CompassData, destination: Destination): Float {
        // todo refactor: move to separate class
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

    private fun parseGeoValue(value: Double?): String =
            value?.let {
                DECIMAL_FORMAT.format(it)
            } ?: "-"

    override fun permissionsNotGranted() {
        view.showPermissionsRequiredInfo()
    }

    override fun gpsNotEnabled() {
        view.showGPSEnabledRequiredInfo()
    }

    override fun onGPSSettingsReturn() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            view.showGPSEnabledRequiredInfo()
        }
    }
}