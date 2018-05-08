package com.fleenmobile.destinationcompass.util.injection.module

import android.content.Context
import android.location.LocationManager
import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import com.fleenmobile.destinationcompass.feature.compass.presentation.MainActivityPresenter
import com.fleenmobile.destinationcompass.feature.compass.view.DestinationFormDialog
import com.fleenmobile.destinationcompass.feature.compass.view.MainActivity
import com.fleenmobile.destinationcompass.util.injection.RuntimeScope
import com.fleenmobile.destinationcompass.util.location.LocationDataProvider
import com.fleenmobile.destinationcompass.util.orientation.OrientationDataProvider
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class MainActivityModule {

    @Provides
    fun view(activity: MainActivity): MainActivityContract.View = activity

    @Provides
    fun locationManager(context: Context): LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @RuntimeScope
    @Provides
    fun presenter(
            view: MainActivityContract.View,
            orientationDataProvider: OrientationDataProvider,
            locationDataProvider: LocationDataProvider,
            compositeDisposable: CompositeDisposable,
            locationManager: LocationManager
    ): MainActivityContract.Presenter =
            MainActivityPresenter(
                    view,
                    orientationDataProvider,
                    locationDataProvider,
                    compositeDisposable,
                    locationManager
            )

    @Provides
    @RuntimeScope
    fun destinationFormDialog(): DestinationFormDialog = DestinationFormDialog()

    @Provides
    @RuntimeScope
    fun rxPermissions(activity: MainActivity): RxPermissions = RxPermissions(activity)
}