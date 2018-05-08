package com.fleenmobile.destinationcompass.feature.compass

import com.fleenmobile.destinationcompass.BaseContract
import com.fleenmobile.destinationcompass.feature.compass.view.Destination

interface MainActivityContract {

    interface View {
        val permissionsEnabled: Boolean
        val isArrowEnabled: Boolean

        fun showDestinationForm()
        fun disableArrow()
        fun enableArrow()
        fun rotateArrow(value: Float)
        fun showDestinationRequiredInfo()
        fun showDestination(latitude: String, longitude: String)
        fun showError(message: String?)
        fun showGPSEnabledRequiredInfo()
        fun showPermissionsRequiredInfo()
    }

    interface Presenter : BaseContract.Presenter {
        fun changeDestinationClicked()
        fun destinationChosen(destination: Destination)
        fun permissionsNotGranted()
        fun gpsNotEnabled()
        fun onGPSSettingsReturn()
    }
}