package com.fleenmobile.destinationcompass.feature.compass.view

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.TransitionDrawable
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.fleenmobile.destinationcompass.R

class CompassView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @BindView(R.id.compass_arrow)
    lateinit var arrowImageView: ImageView

    @BindView(R.id.compass_root)
    lateinit var rootView: ConstraintLayout

    var isActive = false
        set(value) {
            field = value
            animateState()
        }

    init {
        inflate(context, R.layout.compass_view, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ButterKnife.bind(this)
    }

    private fun animateState() {
        val backgroundTransition = rootView.background as TransitionDrawable
        val animationDuration = context.resources.getInteger(R.integer.arrow_animation_time)

        val arrowAnimRes = when (isActive) {
            true -> {
                backgroundTransition.startTransition(animationDuration)
                R.drawable.activate_arrow_animation
            }
            else -> {
                backgroundTransition.reverseTransition(animationDuration)
                R.drawable.deactivate_arrow_animation
            }
        }

        val arrowAnim = ContextCompat.getDrawable(context, arrowAnimRes)
        arrowImageView.setImageDrawable(arrowAnim)
        (arrowAnim as Animatable).start()
    }

    fun rotate(value: Float) =
            arrowImageView.animate()
                    .rotation(value)
                    .start()
}