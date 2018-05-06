package com.fleenmobile.destinationcompass.feature.compass.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.fleenmobile.destinationcompass.R
import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    @BindView(R.id.compassView)
    lateinit var compassView: CompassView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)
    }
}
