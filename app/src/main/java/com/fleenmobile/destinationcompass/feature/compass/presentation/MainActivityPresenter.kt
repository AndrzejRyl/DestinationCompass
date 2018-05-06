package com.fleenmobile.destinationcompass.feature.compass.presentation

import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import com.fleenmobile.destinationcompass.feature.compass.view.Destination
import java.text.DecimalFormat

class MainActivityPresenter(
        private val view: MainActivityContract.View
) : MainActivityContract.Presenter {

    companion object {
        private val DECIMAL_FORMAT = DecimalFormat("0.####")
    }

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
        val (latitude, longitude) = destination
        showDestination(parseGeoValue(latitude), parseGeoValue(longitude))

        if (longitude == null || latitude == null) {
            disableArrow()
            showDestinationRequiredInfo()
        } else {
            // todo calculate rotation
            enableArrow()
            rotateArrow(19f)
        }
    }

    private fun parseGeoValue(value: Double?): String =
            value?.let {
                DECIMAL_FORMAT.format(it)
            } ?: "-"
}