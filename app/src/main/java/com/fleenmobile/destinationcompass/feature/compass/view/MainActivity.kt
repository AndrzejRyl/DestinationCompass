package com.fleenmobile.destinationcompass.feature.compass.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fleenmobile.destinationcompass.R
import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    @BindView(R.id.compassView)
    lateinit var compassView: CompassView

    @Inject
    lateinit var presenter: MainActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)
    }

    //region View
    override fun showDestinationForm() {
        DestinationFormDialog().show(fragmentManager, DestinationFormDialog.TAG)
    }

    override fun disableArrow() {
        compassView.isActive = false
    }

    override fun enableArrow() {
        compassView.isActive = true
    }

    override fun rotateArrow(value: Float) = compassView.rotate(value)

    override fun showDestinationRequiredInfo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //endregion

    @OnClick(R.id.change_destination)
    fun changeDestinationClicked() {
        presenter.changeDestinationClicked()
    }
}
