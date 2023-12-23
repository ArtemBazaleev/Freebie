package com.freebie.frieebiemobile.ui.company.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.databinding.FragmentAccountBinding
import com.freebie.frieebiemobile.databinding.FragmentCompanyBinding
import com.freebie.frieebiemobile.login.GoogleAuth
import com.freebie.frieebiemobile.ui.account.presentation.adapter.AccountAdapter
import com.freebie.frieebiemobile.ui.account.presentation.adapter.AccountClickListener
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountActionButtonUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountState
import com.freebie.frieebiemobile.ui.account.presentation.model.ButtonAction
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyDetailsUiState
import com.freebie.frieebiemobile.ui.company.presentation.model.EMPTY_COMPANY_UI_STATE
import com.freebie.frieebiemobile.ui.coupon.presentation.CouponDetailsFragment
import com.freebie.frieebiemobile.ui.coupon.presentation.model.CouponTransitUIData
import com.freebie.frieebiemobile.ui.feed.adapter.CouponsAdapter
import com.freebie.frieebiemobile.ui.feed.adapter.OffersAdapter
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.utils.gone
import com.freebie.frieebiemobile.ui.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CompanyDetailsFragment : Fragment() {

    private var _binding: FragmentCompanyBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<CompanyDetailsViewModel>()
    private var couponsAdapter: CouponsAdapter? = null
    private var offersAdapter: OffersAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeState()
        val id = arguments?.getString(COMPANY_ID) ?: error("company id required")
        viewModel.requestCompanyDetails(id)
    }


    private fun initAdapter() {
        couponsAdapter = CouponsAdapter {  }
        offersAdapter = OffersAdapter()
        binding.rvCoupons.adapter = couponsAdapter
        binding.rvBooklets.adapter = offersAdapter
    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect(::handleState)
            }
        }
    }

    private fun handleState(state: CompanyDetailsUiState) {
        if (state == EMPTY_COMPANY_UI_STATE) {
            binding.content.gone()
            binding.shimmer.root.visible()
        }
        else {
            binding.shimmer.root.gone()
            binding.content.visible()
        }
        binding.aboutDesc.text = state.description
        Glide.with(requireContext()).load(state.avatar).into(binding.ivCompanyImage)
        couponsAdapter?.submitList(state.coupons)
        offersAdapter?.submitList(state.booklets)
        binding.companyName.text = state.name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        couponsAdapter = null
        offersAdapter = null
    }

    companion object {
        const val COMPANY_ID = "COMPANY_ID"
    }
}