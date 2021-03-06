package com.fleenmobile.destinationcompass.util.injection.binding

import android.support.v7.app.AppCompatActivity
import com.fleenmobile.destinationcompass.feature.compass.view.MainActivity
import com.fleenmobile.destinationcompass.util.injection.RuntimeScope
import com.fleenmobile.destinationcompass.util.injection.module.LocationDataProviderModule
import com.fleenmobile.destinationcompass.util.injection.module.MainActivityModule
import com.fleenmobile.destinationcompass.util.injection.module.OrientationDataProviderModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBinderModule {

    @Binds
    abstract fun activity(activity: AppCompatActivity): AppCompatActivity

    @RuntimeScope
    @ContributesAndroidInjector(modules = [
        MainActivityModule::class,
        LocationDataProviderModule::class,
        OrientationDataProviderModule::class])
    abstract fun bindMainActivity(): MainActivity
}
