package com.fleenmobile.destinationcompass.feature.compass.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.fleenmobile.destinationcompass.R
import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    companion object {
        private const val ENABLE_GPS_REQUEST_CODE = 89
    }

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

    @Inject
    lateinit var rxPermissions: RxPermissions

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    override val permissionsEnabled: Boolean
        get() = rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
                && rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)

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
        compositeDisposable.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ENABLE_GPS_REQUEST_CODE -> {
                presenter.onGPSSettingsReturn()
            }
        }
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
        Snackbar.make(rootView, "Error: $message", Snackbar.LENGTH_LONG).show()
    }

    override fun showGPSEnabledRequiredInfo() =
            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.gps_required))
                    .setMessage(getString(R.string.gps_required_message))
                    .setPositiveButton(getString(R.string.enable_gps)) { dialog, _ ->
                        dialog.dismiss()
                        navigateToGPSSettings()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .setCancelable(false)
                    .create()
                    .show()

    override fun showPermissionsRequiredInfo() =
            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permissions_required))
                    .setMessage(getString(R.string.permisssions_required_message))
                    .setPositiveButton(getString(R.string.give_permissions)) { dialog, _ ->
                        dialog.dismiss()
                        requestPermissions()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .setCancelable(false)
                    .create()
                    .show()

    //endregion

    private fun requestPermissions() {
        compositeDisposable.add(
                rxPermissions
                        .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        .subscribe(
                                { granted ->
                                    if (!granted) {
                                        presenter.permissionsNotGranted()
                                    }
                                },
                                { presenter.permissionsNotGranted() }
                        )
        )
    }

    private fun navigateToGPSSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, ENABLE_GPS_REQUEST_CODE)
    }

    @OnClick(R.id.change_destination)
    fun changeDestinationClicked() {
        presenter.changeDestinationClicked()
    }
}
