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
import com.bumptech.glide.Glide
import com.freebie.frieebiemobile.databinding.FragmentAccountBinding
import com.freebie.frieebiemobile.login.GoogleAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    @Inject
    lateinit var googleAuth: GoogleAuth

    @Inject
    lateinit var toolbarController: AccountToolbarController

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val accountViewModel by viewModels<AccountViewModel>()
    private var openOneTapAuthJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.googleSignIn.setOnClickListener {//todo throtled
            if (openOneTapAuthJob?.isActive == true) return@setOnClickListener
            openOneTapAuthJob = lifecycleScope.launch {
                googleAuth.requestAuth(requireActivity())
            }
        }
        toolbarController.initToolbarAnimation(binding)
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                accountViewModel.state.collect {
                    toolbarController.renderProfile(it.ownProfile, binding)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}