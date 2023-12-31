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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.databinding.FragmentCompanyBinding
import com.freebie.frieebiemobile.deeplinks.DeepLinkHelperImpl
import com.freebie.frieebiemobile.ui.company.presentation.adapter.ExternalLinkAdapter
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyDetailsUiState
import com.freebie.frieebiemobile.ui.company.presentation.model.EMPTY_COMPANY_UI_STATE
import com.freebie.frieebiemobile.ui.coupon.presentation.CouponDetailsFragment
import com.freebie.frieebiemobile.ui.coupon.presentation.model.CouponTransitUIData
import com.freebie.frieebiemobile.ui.feed.adapter.CouponsAdapter
import com.freebie.frieebiemobile.ui.feed.adapter.OffersAdapter
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.rate.presentation.CompanyReviewsFragment
import com.freebie.frieebiemobile.ui.rate.presentation.RateCompanyBottomSheet
import com.freebie.frieebiemobile.ui.rate.presentation.adapter.RateAdapter
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateCompanyTransmitModel
import com.freebie.frieebiemobile.ui.utils.PlaceHolderState
import com.freebie.frieebiemobile.ui.utils.RecyclerPaginationUtil
import com.freebie.frieebiemobile.ui.utils.getNavComponent
import com.freebie.frieebiemobile.ui.utils.gone
import com.freebie.frieebiemobile.ui.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CompanyDetailsFragment : Fragment() {

    private var _binding: FragmentCompanyBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var deepLinkHelper: DeepLinkHelperImpl

    private val viewModel by viewModels<CompanyDetailsViewModel>()
    private var couponsAdapter: CouponsAdapter? = null
    private var offersAdapter: OffersAdapter? = null
    private var externalLinksAdapter: ExternalLinkAdapter? = null
    private var rateAdapter: RateAdapter? = null

    private fun getCompanyId(): String {
        return arguments?.getString(COMPANY_ID) ?: error("company id required")
    }

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
        viewModel.requestCompanyDetails(getCompanyId())
    }


    private fun initAdapter() {
        couponsAdapter = CouponsAdapter {
            openCouponDetails(it)
        }
        offersAdapter = OffersAdapter()
        externalLinksAdapter = ExternalLinkAdapter {
            deepLinkHelper.openDeepLink(it.url)
        }
        rateAdapter = RateAdapter()
        binding.rvCoupons.adapter = couponsAdapter
        binding.rvBooklets.adapter = offersAdapter
        binding.rvExternalLinks.adapter = externalLinksAdapter
        binding.rvRate.adapter = rateAdapter
        initPaging()
    }

    private fun openCouponDetails(it: CouponUI) {
        CouponDetailsFragment.show(
            childFragmentManager, CouponTransitUIData(
                it.id, it.description, it.avatar,
                it.name, it.priceWithDiscount, it.price
            )
        )
    }

    private fun initPaging() {
        binding.rvCoupons.addOnScrollListener(
            RecyclerPaginationUtil(
                binding.rvCoupons.layoutManager as LinearLayoutManager,
                viewModel.getCouponsPagingCallback(),
                threshHold = 5
            )
        )

        binding.rvBooklets.addOnScrollListener(
            RecyclerPaginationUtil(
                binding.rvBooklets.layoutManager as LinearLayoutManager,
                viewModel.getBookletsPagingCallback(),
                threshHold = 5
            )
        )
    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect(::handleState)
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.placeholderState.collect(::handlePlaceholder)
            }
        }
    }

    private fun handlePlaceholder(placeHolderState: PlaceHolderState?) {
        if (placeHolderState == null) {
            binding.companyPlaceholder.gone()
        } else {
            binding.companyPlaceholder.visible()
            binding.content.gone()
            binding.shimmer.root.gone()
            binding.companyPlaceholder.setState(placeHolderState, true) {
                binding.shimmer.root.visible()
                viewModel.requestCompanyDetails(getCompanyId())
            }
        }
    }

    private fun handleState(state: CompanyDetailsUiState) {
        Log.d("CompanyDetailsFragment", "observed state = $state")
        if (state == EMPTY_COMPANY_UI_STATE) {
            binding.content.gone()
            binding.shimmer.root.visible()
        } else {
            binding.shimmer.root.gone()
            binding.content.visible()
        }
        binding.aboutDesc.text = state.description
        Glide.with(requireContext()).load(state.avatar).into(binding.ivCompanyImage)
        couponsAdapter?.submitList(state.coupons)
        offersAdapter?.submitList(state.booklets)
        externalLinksAdapter?.submitList(state.externalLinks)
        rateAdapter?.submitList(state.rateList)
        binding.companyName.text = state.name
        binding.companyRating.text = state.rating.toString()
        if (state.showMoreComment) binding.moreComments.visible()
        else binding.moreComments.gone()
        if (state.canRate) binding.addRating.visible()
        else binding.addRating.gone()
        binding.addRating.setOnClickListener {
            RateCompanyBottomSheet.show(childFragmentManager, RateCompanyTransmitModel(getCompanyId(), state.name)) {
                viewModel.requestCompanyDetails(getCompanyId())
            }
        }
        binding.moreComments.setOnClickListener {
            activity?.getNavComponent()?.navigate(
                R.id.navigation_company_reviews,
                Bundle().apply {
                     putString(CompanyReviewsFragment.COMPANY_ID, getCompanyId())
                }
            )
        }
        if (state.rating == 0.0 && !state.canRate)
            binding.ratingBar.gone()
        else binding.ratingBar.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        couponsAdapter = null
        offersAdapter = null
        externalLinksAdapter = null
        rateAdapter = null
    }

    companion object {
        const val COMPANY_ID = "COMPANY_ID"
    }
}