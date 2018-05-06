package com.fleenmobile.destinationcompass.feature.compass

import com.fleenmobile.destinationcompass.BaseContract

interface MainActivityContract {

    interface View {
        fun showDestinationForm()
        fun disableArrow()
        fun enableArrow()
        fun rotateArrow(value: Float)
        fun showDestinationRequiredInfo()
    }

    interface Presenter : BaseContract.Presenter {
        fun changeDestinationClicked()
        fun destinationChosen(longitude: Double?, latitude: Double?)
    }
}