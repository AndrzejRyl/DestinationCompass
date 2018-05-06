package com.fleenmobile.destinationcompass.feature.compass.presentation

import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import com.fleenmobile.destinationcompass.feature.compass.view.Destination

class MainActivityPresenter(
        private val view: MainActivityContract.View
) : MainActivityContract.Presenter {

    override fun initialize() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeDestinationClicked() {
        view.showDestinationForm()
    }

    override fun destinationChosen(destination: Destination) = with(view) {
        val (longitude, latitude) = destination

        if (longitude == null || latitude == null) {
            disableArrow()
            showDestinationRequiredInfo()
            return
        } else {
            // todo calculate rotation
            enableArrow()
            rotateArrow(19f)
        }
    }
}