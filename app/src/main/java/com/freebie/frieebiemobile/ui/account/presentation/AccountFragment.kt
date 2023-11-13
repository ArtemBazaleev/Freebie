package com.freebie.frieebiemobile.ui.account.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.freebie.frieebiemobile.databinding.FragmentAccountBinding
import com.freebie.frieebiemobile.login.GoogleAuth
import com.freebie.frieebiemobile.ui.account.presentation.adapter.AccountAdapter
import com.freebie.frieebiemobile.ui.account.presentation.adapter.AccountClickListener
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountActionButtonUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.ButtonAction
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
    }

    override fun actionButtonClick(model: AccountActionButtonUIModel) {
        when (model.buttonAction) {
            ButtonAction.GoogleSignIn -> {
                if (openOneTapAuthJob?.isActive == true) return
                openOneTapAuthJob = lifecycleScope.launch {
                    googleAuth.requestAuth(requireActivity())
                }
            }
            null -> TODO()
        }

    }

    private fun initAdapter() {
        accountAdapter = AccountAdapter(this)
        binding.accontRecycler.adapter = accountAdapter
    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                accountViewModel.state.collect {
                    binding.accountRefreshLayout.isRefreshing = it.isRefreshing
                    accountAdapter?.submitList(it.accountUI)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.accontRecycler.adapter = null
        _binding = null
    }
}