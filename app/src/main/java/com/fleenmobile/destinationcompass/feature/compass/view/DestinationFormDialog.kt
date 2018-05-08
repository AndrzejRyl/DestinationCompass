package com.fleenmobile.destinationcompass.feature.compass.view

import android.app.DialogFragment
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.fleenmobile.destinationcompass.R
import com.fleenmobile.destinationcompass.parseDouble

data class Destination(val latitude: Double?, val longitude: Double?)

typealias OnDestinationChosenCallback = (destination: Destination) -> Unit

class DestinationFormDialog : DialogFragment() {

    companion object {
        val TAG = DestinationFormDialog::class.java.simpleName
    }

    @BindView(R.id.destination_form_longitude)
    lateinit var longitudeEditText: EditText

    @BindView(R.id.destination_form_latitude)
    lateinit var latitudeEditText: EditText

    lateinit var latitudeInputFilter: InputFilter
    lateinit var longitudeInputFilter: InputFilter

    private lateinit var unbinder: Unbinder

    var onDestinationChosenCallback: OnDestinationChosenCallback? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_destination_form_dialog, container, false)
        unbinder = ButterKnife.bind(this, view)
        isCancelable = false
        dialog.window.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawableResource(android.R.color.transparent)
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }

        attachInputFilter(latitudeEditText, latitudeInputFilter)
        attachInputFilter(longitudeEditText, longitudeInputFilter)
        return view
    }

    private fun attachInputFilter(editText: EditText, inputFilter: InputFilter) {
        val editTextFilters = arrayListOf<InputFilter>()
        editTextFilters.addAll(editText.filters)
        editTextFilters.add(inputFilter)
        editTextFilters.toArray(editText.filters)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.destination_form_confirm)
    fun onOkClicked() {
        val latitude = latitudeEditText.text.toString().parseDouble()
        val longitude = longitudeEditText.text.toString().parseDouble()

        onDestinationChosenCallback?.invoke(Destination(latitude, longitude))
        dismiss()
    }
}