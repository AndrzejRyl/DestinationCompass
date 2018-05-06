package com.fleenmobile.destinationcompass.feature.compass.view

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.fleenmobile.destinationcompass.R

class DestinationFormDialog : DialogFragment() {

    companion object {
        val TAG = DestinationFormDialog::class.java.simpleName
    }

    private lateinit var unbinder: Unbinder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_destination_form_dialog, container, false)
        unbinder = ButterKnife.bind(this, view)
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
        dismiss()
    }
}