package com.fleenmobile.destinationcompass.util.injection.module

import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import com.fleenmobile.destinationcompass.feature.compass.presentation.MainActivityPresenter
import com.fleenmobile.destinationcompass.feature.compass.view.DestinationFormDialog
import com.fleenmobile.destinationcompass.feature.compass.view.MainActivity
import com.fleenmobile.destinationcompass.util.injection.RuntimeScope
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun view(activity: MainActivity): MainActivityContract.View = activity

    @RuntimeScope
    @Provides
    fun presenter(
            view: MainActivityContract.View
    ): MainActivityContract.Presenter =
            MainActivityPresenter(view)

    @Provides
    @RuntimeScope
    fun destinationFormDialog(): DestinationFormDialog = DestinationFormDialog()
}