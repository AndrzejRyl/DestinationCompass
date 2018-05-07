package com.fleenmobile.destinationcompass.feature.compass.view

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fleenmobile.destinationcompass.R
import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    @BindView(R.id.compass_view)
    lateinit var compassView: CompassView

    @BindView(R.id.root_view)
    lateinit var rootView: ConstraintLayout

    @BindView(R.id.destination_value)
    lateinit var destinationTextView: TextView

    @Inject
    lateinit var presenter: MainActivityContract.Presenter

    @Inject
    lateinit var destinationFormDialog: DestinationFormDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)
        initDestinationFormCallbacks()

        presenter.initialize()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clear()
    }

    private fun initDestinationFormCallbacks() {
        destinationFormDialog.onDestinationChosenCallback = presenter::destinationChosen
    }

    //region View
    override fun showDestinationForm() {
        destinationFormDialog.show(fragmentManager, DestinationFormDialog.TAG)
    }

    override fun disableArrow() {
        compassView.isActive = false
    }

    override fun enableArrow() {
        compassView.isActive = true
    }

    override fun rotateArrow(value: Float) = compassView.rotate(value)

    override fun showDestinationRequiredInfo() {
        Snackbar
                .make(rootView, getString(R.string.destination_required), Snackbar.LENGTH_LONG)
                .apply {
                    setAction(getString(R.string.choose), { _ -> presenter.changeDestinationClicked() })
                    show()
                }
    }

    override fun showDestination(latitude: String, longitude: String) {
        val destinationString = getString(R.string.destination_format, latitude, longitude)
        destinationTextView.text = destinationString
    }

    override fun showError(message: String?) {
        //todo show snackbar
    }

    //endregion

    @OnClick(R.id.change_destination)
    fun changeDestinationClicked() {
        presenter.changeDestinationClicked()
    }
}
