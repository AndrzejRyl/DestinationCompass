package com.fleenmobile.destinationcompass.feature.compass.presentation

import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract

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

    override fun destinationChosen(longitude: Double?, latitude: Double?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}