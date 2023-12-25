package com.freebie.frieebiemobile.ui.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.freebie.frieebiemobile.R

class PlaceHolderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var text: TextView? = null
    private var refreshButton: CardView? = null
    private var lottieAnimation: LottieAnimationView? = null

    init {
        inflate(context, R.layout.layout_placeholder, this)
        text = findViewById(R.id.header_text_placeholder)
        lottieAnimation = findViewById(R.id.lottie_animation)
        refreshButton = findViewById(R.id.refresh_btn)
    }

    fun setState(
        state: PlaceHolderState,
        needToAnimate: Boolean,
        refreshListener: (() -> Unit)? = null
    ) {
        lottieAnimation?.setAnimation(state.lottieRes)
        if (needToAnimate) lottieAnimation?.playAnimation()
        text?.apply {
            visibility = if (state.text == null) INVISIBLE
            else VISIBLE
            state.text?.let { setText(it) }
        }
        refreshListener?.let { refresh ->
            refreshButton?.visible()
            refreshButton?.setOnClickListener { refresh.invoke() }
        } ?: kotlin.run {
            refreshButton?.gone()
        }
    }
}

enum class PlaceHolderState(
    val lottieRes: Int,
    val text: Int?
) {
    NoInternet(
        lottieRes = R.raw.no_internet_1,
        text = null
    ),

    NoData(
        lottieRes = R.raw.no_data,
        text = R.string.no_data
    ),

    SomethingWentWrong(
        lottieRes = R.raw.general_error,
        text = R.string.generic_error_message
    )
}
