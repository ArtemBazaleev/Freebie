package com.freebie.frieebiemobile.ui.coupon.presentation

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.databinding.FragmentCouponDetailsBinding
import com.freebie.frieebiemobile.ui.coupon.presentation.model.CouponDetailsState
import com.freebie.frieebiemobile.ui.coupon.presentation.model.CouponTransitUIData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

@AndroidEntryPoint
class CouponDetailsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCouponDetailsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<CouponDetailsViewModel>()

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.originalPrice.paintFlags = binding.originalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.appCompatImageView.clipToOutline = true
        initStateObserver()
        return root
    }

    private fun initStateObserver() {
        val id = arguments?.getString(COUPON_ID_KEY) ?: throw IllegalStateException("provide transit data")
        viewModel.init(id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect(::handleState)
            }
        }
    }

    private fun handleState(state: CouponDetailsState) {
        if (state.couponDetails == null) return
        binding.couponHeader.text = state.couponDetails.title
        binding.couponDescription.text = state.couponDetails.description
        binding.discountPrice.text = state.couponDetails.priceWithDiscount
        binding.originalPrice.text = state.couponDetails.priceWithoutDiscount
        Glide.with(binding.appCompatImageView)
            .load(state.couponDetails.imageUrl)
            .into(binding.appCompatImageView)
        binding.buyCoupon.text = state.couponDetails.couponPriceText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CouponDetailsFragment"
        private const val COUPON_ID_KEY = "COUPON_ID"


        fun show(fm: FragmentManager, transitUIData: CouponTransitUIData) {
            if (fm.findFragmentByTag(TAG) != null) return
            val fragment = CouponDetailsFragment()
            val bundle = Bundle().apply {
                putString(COUPON_ID_KEY, transitUIData.couponId)
            }
            fragment.arguments = bundle
            fragment.show(fm, TAG)
        }
    }
}