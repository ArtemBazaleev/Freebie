package com.freebie.frieebiemobile.ui.account.presentation

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
import androidx.recyclerview.widget.SimpleItemAnimator
import com.freebie.frieebiemobile.databinding.FragmentAccountBinding
import com.freebie.frieebiemobile.login.GoogleAuth
import com.freebie.frieebiemobile.ui.account.presentation.adapter.AccountAdapter
import com.freebie.frieebiemobile.ui.account.presentation.adapter.AccountClickListener
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountActionButtonUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountState
import com.freebie.frieebiemobile.ui.account.presentation.model.ButtonAction
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel
import com.freebie.frieebiemobile.ui.coupon.presentation.CouponDetailsFragment
import com.freebie.frieebiemobile.ui.coupon.presentation.model.CouponTransitUIData
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.utils.gone
import com.freebie.frieebiemobile.ui.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment(), AccountClickListener{

    private var _binding: FragmentAccountBinding? = null

    @Inject
    lateinit var googleAuth: GoogleAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val accountViewModel by viewModels<AccountViewModel>()
    private var openOneTapAuthJob: Job? = null
    private var accountAdapter: AccountAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root
        _binding?.accountRefreshLayout?.isRefreshing = true
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeState()
        initView()
    }

    private fun initView() {
        binding.accountRefreshLayout.setOnRefreshListener {
            accountViewModel.refresh()
        }

        binding.ivExit.setOnClickListener {
            accountViewModel.logout()
        }
    }

    override fun actionButtonClick(model: AccountActionButtonUIModel) {
        when (model.buttonAction) {
            ButtonAction.GoogleSignIn -> {
                if (openOneTapAuthJob?.isActive == true) return
                openOneTapAuthJob = lifecycleScope.launch {
                    googleAuth.requestAuth(requireActivity())
                }
            }
            ButtonAction.Logout -> {
                accountViewModel.logout()
            }
            else -> {}
        }

    }

    private fun showCouponDetails(transitUIData: CouponTransitUIData) {
        CouponDetailsFragment.show(childFragmentManager, transitUIData)
    }

    override fun couponGroupClick(group: CouponGroupUiModel) {
        accountViewModel.onCouponGroupClicked(group)
    }

    override fun onCouponClicked(coupon: CouponUI) {
        showCouponDetails(
            CouponTransitUIData(
                couponId = coupon.id,
                description = coupon.description,
                image = coupon.avatar,
                header = coupon.name,
                priceWithDiscount = coupon.priceWithDiscount,
                priceWithoutDiscount = coupon.price

            )
        )
    }

    private fun initAdapter() {
        accountAdapter = AccountAdapter(this)
        binding.accontRecycler.adapter = accountAdapter
        binding.accontRecycler.itemAnimator = null
    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                accountViewModel.state.collect {
                    Log.d("observeState","observeState $it")
                    accountAdapter?.submitList(it.accountUI)
                    handlePlaceHolder(it)
                    handleShimmer(it)
                    if (it.isAuthed) binding.ivExit.visible()
                    else binding.ivExit.gone()
                }
            }
        }
    }

    private fun handleShimmer(state: AccountState) {
        Log.d("AccountViewModel", "emitAccountState0 = $state")
        binding.accountRefreshLayout.isRefreshing = state.isRefreshing
        if (state.accountUI.isNotEmpty() || !state.isRefreshing) {
            binding.shimmer.shimmerLayout.stopShimmer()
            binding.shimmer.root.visibility = View.GONE
        } else {
            binding.shimmer.shimmerLayout.startShimmer()
            binding.shimmer.root.visibility = View.VISIBLE
        }
    }

    private fun handlePlaceHolder(state: AccountState) {
        if (state.accountUI.isNotEmpty() || state.placeholder?.state == null || state.isRefreshing) {
            binding.placeholder.visibility = View.GONE
        } else {
            binding.placeholder.visibility = View.VISIBLE
            binding.placeholder.setState(state.placeholder.state, state.placeholder.needToAnimate)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.accontRecycler.adapter = null
        _binding = null
    }
}