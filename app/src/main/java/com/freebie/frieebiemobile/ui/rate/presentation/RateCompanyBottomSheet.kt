package com.freebie.frieebiemobile.ui.rate.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.databinding.FragmentRateCompanyBinding
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateCompanyEvent
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateCompanyTransmitModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

@AndroidEntryPoint
class RateCompanyBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentRateCompanyBinding? = null
    private val viewModel by viewModels<RateCompanyViewModel>()

    private val binding get() = _binding!!
    private var successListener: WeakReference<() -> Unit>? = null

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRateCompanyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initObserver()
        initView()
        return root
    }

    private fun initView() {
        binding.couponHeader.text =
            getString(R.string.rate_company_name, arguments?.getString(COMPANY_NAME_KEY))
        binding.tvRate.setOnClickListener { sendReview() }
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, _ ->
            if (rating.toInt() == 0) ratingBar.rating = 1f
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.events.collect(::handleEvents)
            }
        }
    }

    private fun sendReview() {
        val message = binding.reviewMessage.text.toString().trim()
        val rating = binding.ratingBar.rating.toInt()
        val companyId = arguments?.getString(COMPANY_ID_KEY) ?: error("Need company id for review")
        viewModel.rateCompany(message, rating, companyId)
    }

    private fun handleEvents(event: RateCompanyEvent) {
        when (event) {
            RateCompanyEvent.RateFailed -> {
                val message = getString(R.string.generic_error_message)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

            RateCompanyEvent.RateSuccess -> {
                val message = getString(R.string.success_review_send)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                successListener?.get()?.invoke()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "RateCompanyBottomSheet"
        private const val COMPANY_ID_KEY = "COMPANY_ID_KEY"
        private const val COMPANY_NAME_KEY = "COMPANY_NAME_KEY"


        fun show(
            fm: FragmentManager,
            transitUIData: RateCompanyTransmitModel,
            successListener: () -> Unit
        ) {
            if (fm.findFragmentByTag(TAG) != null) return
            val fragment = RateCompanyBottomSheet()
            fragment.setSuccessListener(successListener)
            val bundle = Bundle().apply {
                putString(COMPANY_ID_KEY, transitUIData.companyId)
                putString(COMPANY_NAME_KEY, transitUIData.companyName)
            }
            fragment.arguments = bundle
            fragment.show(fm, TAG)
        }
    }

    private fun setSuccessListener(successListener: () -> Unit) {
        this.successListener = WeakReference(successListener)
    }
}