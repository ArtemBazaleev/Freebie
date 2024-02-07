package com.freebie.frieebiemobile.ui.rate.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.freebie.frieebiemobile.databinding.FragmentCompanyReviewsBinding
import com.freebie.frieebiemobile.ui.rate.presentation.adapter.RateAdapter
import com.freebie.frieebiemobile.ui.rate.presentation.model.CompanyReviewUIState
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateCompanyResponseUIModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateUIModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.ReplyCompanyTransitModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.UserRateUiModel
import com.freebie.frieebiemobile.ui.utils.RecyclerPaginationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompanyReviewsFragment : Fragment() {

    private var _binding: FragmentCompanyReviewsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<CompanyReviewsViewModel>()
    private var adapter: RateAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val companyId = arguments?.getString(COMPANY_ID) ?: error("Need company id")
        val canModerate = arguments?.getBoolean(CAN_MODERATE) ?: false
        viewModel.initViewModel(companyId, canModerate)
    }

    private fun getCompanyId(): String =
        arguments?.getString(COMPANY_ID) ?: error("Need company id")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompanyReviewsBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect(::handleState)
            }
        }
    }

    private fun handleState(state: CompanyReviewUIState) {
        adapter?.submitList(state.reviewList)
        binding.refreshLayout.isRefreshing = state.reviewList.isEmpty()
    }

    private fun initView() {
        adapter = RateAdapter { model ->
            handleClick(model)
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.rvReviews.adapter = adapter
        binding.rvReviews.addOnScrollListener(
            RecyclerPaginationUtil(
                pagingCallback = viewModel.getPagingCallback(),
                layoutManager = binding.rvReviews.layoutManager as LinearLayoutManager
            )
        )
    }

    private fun handleClick(model: RateUIModel) {
        when (model) {
            is RateCompanyResponseUIModel -> {}
            is UserRateUiModel -> {
                ReplyReviewBottomSheet.show(
                    fm = childFragmentManager,
                    transitData = ReplyCompanyTransitModel(model)
                ) { message ->
                    viewModel.replyComment(message, getCompanyId())
                }
            }
        }
    }

    companion object {
        const val COMPANY_ID = "COMPANY_ID"
        const val CAN_MODERATE = "CAN_MODERATE"
    }
}