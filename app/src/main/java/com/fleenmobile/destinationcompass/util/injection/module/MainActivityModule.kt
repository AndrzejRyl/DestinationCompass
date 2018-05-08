package com.fleenmobile.destinationcompass.util.injection.module

import android.content.Context
import android.location.LocationManager
import android.text.InputFilter
import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import com.fleenmobile.destinationcompass.feature.compass.presentation.MainActivityPresenter
import com.fleenmobile.destinationcompass.feature.compass.view.DestinationFormDialog
import com.fleenmobile.destinationcompass.feature.compass.view.MainActivity
import com.fleenmobile.destinationcompass.util.injection.RuntimeScope
import com.fleenmobile.destinationcompass.util.location.LocationDataProvider
import com.fleenmobile.destinationcompass.util.orientation.OrientationDataProvider
import com.fleenmobile.destinationcompass.util.rotation.RotationHelper
import com.fleenmobile.destinationcompass.util.rotation.RotationHelperImpl
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

@Module
class MainActivityModule {

    @Provides
    fun view(activity: MainActivity): MainActivityContract.View = activity

    @Provides
    fun locationManager(context: Context): LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    fun rotationHelper(): RotationHelper = RotationHelperImpl()

    @RuntimeScope
    @Provides
    fun presenter(
            view: MainActivityContract.View,
            orientationDataProvider: OrientationDataProvider,
            locationDataProvider: LocationDataProvider,
            compositeDisposable: CompositeDisposable,
            locationManager: LocationManager,
            rotationHelper: RotationHelper
    ): MainActivityContract.Presenter =
            MainActivityPresenter(
                    view,
                    orientationDataProvider,
                    locationDataProvider,
                    compositeDisposable,
                    locationManager,
                    rotationHelper
            )

    private fun provideInputFilter(min: Double, max: Double) = InputFilter { source, _, _, dest, _, _ ->
        try {
            val input = "$dest$source".toDouble()
            if (input in min..max) {
                return@InputFilter null
            }
        } catch (e: NumberFormatException) {

        }
        return@InputFilter ""

    }

    @Provides
    @Named("latitudeInputFilter")
    fun latitudeInputFilter(): InputFilter = provideInputFilter(-90.0, 90.0)

    @Provides
    @Named("longitudeInputFilter")
    fun longitudeInputFilter(): InputFilter = provideInputFilter(-180.0, 180.0)

    @Provides
    @RuntimeScope
    fun destinationFormDialog(
            @Named("latitudeInputFilter") latitudeInputFilter: InputFilter,
            @Named("longitudeInputFilter") longitudeInputFilter: InputFilter
    ): DestinationFormDialog =
            DestinationFormDialog().apply {
                this.latitudeInputFilter = latitudeInputFilter
                this.longitudeInputFilter = longitudeInputFilter
            }

    @Provides
    @RuntimeScope
    fun rxPermissions(activity: MainActivity): RxPermissions = RxPermissions(activity)
}