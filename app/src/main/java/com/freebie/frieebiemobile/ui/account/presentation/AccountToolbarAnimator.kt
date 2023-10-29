package com.freebie.frieebiemobile.ui.account.presentation

import android.util.TypedValue
import android.view.View
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.databinding.FragmentAccountBinding
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt

class AccountToolbarController @Inject constructor() {

    private var EXPAND_AVATAR_SIZE = 0F
    private var COLLAPSE_IMAGE_SIZE = 0F
    private var horizontalToolbarAvatarMargin = 0F
    private var cashCollapseState: Pair<Int, Int>? = null
    private var avatarAnimateStartPointY: Float = 0F
    private var avatarCollapseAnimationChangeWeight: Float = 0F
    private var isCalculated = false
    private var verticalToolbarAvatarMargin =0F


    fun initToolbarAnimation(binding: FragmentAccountBinding) {
        val resources = binding.root.resources
        EXPAND_AVATAR_SIZE = resources.getDimension(R.dimen.default_expanded_image_size)
        COLLAPSE_IMAGE_SIZE = resources.getDimension(R.dimen.default_collapsed_image_size)
        horizontalToolbarAvatarMargin = resources.getDimension(R.dimen.activity_margin)
        (binding.animToolbar.height - COLLAPSE_IMAGE_SIZE) * 2
        /**/
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, i ->
            if (isCalculated.not()) {
                avatarAnimateStartPointY =
                    abs((appBarLayout.height - (EXPAND_AVATAR_SIZE + horizontalToolbarAvatarMargin)) / appBarLayout.totalScrollRange)
                avatarCollapseAnimationChangeWeight = 1 / (1 - avatarAnimateStartPointY)
                verticalToolbarAvatarMargin = (binding.animToolbar.height - COLLAPSE_IMAGE_SIZE) * 2
                isCalculated = true
            }
            val offset = abs(i / appBarLayout.totalScrollRange.toFloat())
            updateViews(offset, binding)
        }
    }


    private fun updateViews(offset: Float, binding: FragmentAccountBinding) {
        /* apply levels changes*/
        when (offset) {
            in 0.15F..1F -> {
                binding.tvProfileName.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
            }

            in 0F..0.15F -> {
                binding.tvProfileName.alpha = (1f)
                binding.imgbAvatarWrap.alpha = 1f
            }
        }

        /** collapse - expand switch*/
        when {
            offset < SWITCH_BOUND -> Pair(TO_EXPANDED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
            else -> Pair(TO_COLLAPSED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
        }.apply {
            when {
                cashCollapseState != null && cashCollapseState != this -> {
                    when (first) {
                        TO_EXPANDED ->  {
                            binding.imgbAvatarWrap.translationX = 0F
                            binding.tvProfileNameSingle.visibility = View.INVISIBLE
                        }
                        TO_COLLAPSED ->  {
                            /* show titles on toolbar with animation*/
                            binding.tvProfileNameSingle.apply {
                                visibility = View.VISIBLE
                                alpha = 0F
                                animate().setDuration(500).alpha(1.0f)
                            }
                        }
                    }
                    cashCollapseState = Pair(first, SWITCHED)
                }
                else -> {
                    cashCollapseState = Pair(first, WAIT_FOR_SWITCH)
                }
            }

            /* Collapse avatar img*/
            binding.imgbAvatarWrap.apply {
                when {
                    offset > avatarAnimateStartPointY -> {
                        val avatarCollapseAnimateOffset = (offset - avatarAnimateStartPointY) * avatarCollapseAnimationChangeWeight
                        val avatarSize = EXPAND_AVATAR_SIZE - (EXPAND_AVATAR_SIZE - COLLAPSE_IMAGE_SIZE) * avatarCollapseAnimateOffset
                        this.layoutParams.also {
                            it.height = avatarSize.roundToInt()
                            it.width = avatarSize.roundToInt()
                        }
                        binding.tvWorkaround.setTextSize(TypedValue.COMPLEX_UNIT_PX, offset)

                        this.translationX = ((binding.appBarLayout.width - horizontalToolbarAvatarMargin - avatarSize) / 2) * avatarCollapseAnimateOffset
                        this.translationY = ((binding.animToolbar.height  - verticalToolbarAvatarMargin - avatarSize ) / 2) * avatarCollapseAnimateOffset
                    }
                    else -> this.layoutParams.also {
                        if (it.height != EXPAND_AVATAR_SIZE.toInt()) {
                            it.height = EXPAND_AVATAR_SIZE.toInt()
                            it.width = EXPAND_AVATAR_SIZE.toInt()
                            this.layoutParams = it
                        }
                        translationX = 0f
                    }
                }
            }
        }
    }

    companion object {
        const val SWITCH_BOUND = 0.8f
        const val TO_EXPANDED = 0
        const val TO_COLLAPSED = 1
        const val WAIT_FOR_SWITCH = 0
        const val SWITCHED = 1
    }
}