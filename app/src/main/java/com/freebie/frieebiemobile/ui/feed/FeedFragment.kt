package com.freebie.frieebiemobile.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.freebie.frieebiemobile.databinding.FragmentFeedBinding
import com.freebie.frieebiemobile.ui.coupon.presentation.CouponDetailsFragment
import com.freebie.frieebiemobile.ui.coupon.presentation.model.CouponTransitUIData
import com.freebie.frieebiemobile.ui.feed.adapter.FeedAdapter
import com.freebie.frieebiemobile.ui.feed.adapter.FeedClickListener
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.feed.models.FeedItem
import com.freebie.frieebiemobile.ui.feed.models.FeedShimmer
import com.freebie.frieebiemobile.ui.feed.models.FeedState
import com.freebie.frieebiemobile.ui.utils.PlaceHolderState
import com.freebie.frieebiemobile.ui.utils.RecyclerPaginationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FeedFragment : Fragment(), FeedClickListener {

    private var _binding: FragmentFeedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val feedViewModel by viewModels<FeedViewModel>()
    private var feedAdapter: FeedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        initSwipeToRefresh()
        observeData()
    }

    private fun initToolbar() {
        if (arguments != null) {

        }
    }

    private fun initSwipeToRefresh() {
        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = false
            feedViewModel.refresh()
        }
    }

    private fun initAdapter() {
        feedAdapter = FeedAdapter(this)
        binding.rvFeed.adapter = feedAdapter
        binding.rvFeed.addOnScrollListener(RecyclerPaginationUtil(
            binding.rvFeed.layoutManager as LinearLayoutManager,
            feedViewModel
        ))
    }

    private fun observeData() {
        feedViewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach(::handleState)
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: FeedState) {
        feedAdapter?.submitList(state.feedList)
        if (isValidForPlaceholder(state)) {
            binding.placeholder.visibility = View.VISIBLE
            binding.placeholder.setState(state.placeHolderInfo.state!!, state.placeHolderInfo.needToAnimate)
        } else {
            binding.placeholder.visibility = View.GONE
        }
    }

    private fun isValidForPlaceholder(state: FeedState): Boolean {
        val isLoadingState = state.feedList.size == 1 && state.feedList[0] is FeedShimmer
        return state.placeHolderInfo.state != null && (isLoadingState || state.feedList.isEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        feedAdapter = null
    }

    override fun onCouponClicked(couponUI: CouponUI) {
        CouponDetailsFragment.show(childFragmentManager, CouponTransitUIData(couponUI.id))
    }
}