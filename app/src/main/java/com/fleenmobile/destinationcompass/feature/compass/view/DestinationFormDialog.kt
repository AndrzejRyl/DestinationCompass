package com.fleenmobile.destinationcompass.feature.compass.view

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.fleenmobile.destinationcompass.R
import com.fleenmobile.destinationcompass.parseDouble

data class Destination(val longitude: Double?, val latitude: Double?)

typealias OnDestinationChosenCallback = (destination: Destination) -> Unit

class DestinationFormDialog : DialogFragment() {

    companion object {
        val TAG = DestinationFormDialog::class.java.simpleName
    }

    @BindView(R.id.destination_form_longitude)
    lateinit var longitudeEditText: EditText

    @BindView(R.id.destination_form_latitude)
    lateinit var latitudeEditText: EditText

    private lateinit var unbinder: Unbinder

    var onDestinationChosenCallback: OnDestinationChosenCallback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_destination_form_dialog, container, false)
        unbinder = ButterKnife.bind(this, view)
        isCancelable = false
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.destination_form_confirm)
    fun onOkClicked() {
        val longitude = longitudeEditText.text.toString().parseDouble()
        val latitude = latitudeEditText.text.toString().parseDouble()

        onDestinationChosenCallback?.invoke(Destination(longitude, latitude))
        dismiss()
    }
}