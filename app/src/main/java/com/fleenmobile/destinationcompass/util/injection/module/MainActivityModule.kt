package com.fleenmobile.destinationcompass.util.injection.module

import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import com.fleenmobile.destinationcompass.feature.compass.presentation.MainActivityPresenter
import com.fleenmobile.destinationcompass.feature.compass.view.DestinationFormDialog
import com.fleenmobile.destinationcompass.feature.compass.view.MainActivity
import com.fleenmobile.destinationcompass.util.injection.RuntimeScope
import com.fleenmobile.destinationcompass.util.location.LocationDataProvider
import com.fleenmobile.destinationcompass.util.orientation.OrientationDataProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class MainActivityModule {

    @Provides
    fun view(activity: MainActivity): MainActivityContract.View = activity

    @RuntimeScope
    @Provides
    fun presenter(
            view: MainActivityContract.View,
            orientationDataProvider: OrientationDataProvider,
            locationDataProvider: LocationDataProvider,
            compositeDisposable: CompositeDisposable
    ): MainActivityContract.Presenter =
            MainActivityPresenter(view, orientationDataProvider, locationDataProvider, compositeDisposable)

    @Provides
    @RuntimeScope
    fun destinationFormDialog(): DestinationFormDialog = DestinationFormDialog()
}