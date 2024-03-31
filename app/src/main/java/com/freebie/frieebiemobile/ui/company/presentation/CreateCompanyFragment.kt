package com.freebie.frieebiemobile.ui.company.presentation

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.databinding.FragmentCreateCompanyBinding
import com.freebie.frieebiemobile.network.DEFAULT_LOCALE
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalCompanyLink
import com.freebie.frieebiemobile.ui.company.domain.model.ExternalLinkType
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyCreationEvent
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyCreationUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCompanyFragment : Fragment() {

    private var _binding: FragmentCreateCompanyBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<CreateCompanyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCompanyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeEvents()
        binding.createCompany.setOnClickListener {
            viewModel.createCompany()
        }
        addTextListeners()
    }

    private fun addTextListeners() {
        binding.etCompanyDescription.doAfterTextChanged { text: Editable? ->
            addLocale()
        }

        binding.etCompanyName.doAfterTextChanged {
            addLocale()
        }

        binding.include.etTelegram.doAfterTextChanged {
            viewModel.addLink(ExternalCompanyLink(it.toString(), ExternalLinkType.TELEGRAM))
        }

        binding.include.etInstagram.doAfterTextChanged {
            viewModel.addLink(ExternalCompanyLink(it.toString(), ExternalLinkType.INSTAGRAM))
        }

        binding.include.etWhatsapp.doAfterTextChanged {
            viewModel.addLink(ExternalCompanyLink(it.toString(), ExternalLinkType.WHATSAPP))
        }
    }

    private fun addLocale() = viewModel.addLocale(
        langCode = resources.configuration.locales[0].language ?: DEFAULT_LOCALE,
        description = binding.etCompanyDescription.text.toString(),
        name = binding.etCompanyName.text.toString()
    )

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::handleState)
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect(::handleEvents)
            }
        }
    }

    private fun handleEvents(event: CompanyCreationEvent) {
        when(event) {
            CompanyCreationEvent.CloseSelf -> activity?.onBackPressedDispatcher?.onBackPressed()
            CompanyCreationEvent.ErrorWhileCreatingCompany -> Toast.makeText(
                requireContext(),
                "Error while creating company",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleState(state: CompanyCreationUiModel) {
        initDropDownMenuCategories(state.categories.map { it.name })
        initDropDownMenuCities(state.cities)
        handleErrors(state)
    }

    private fun handleErrors(state: CompanyCreationUiModel) {
        if (state.errorCompanyName?.resId != null) {
            binding.layoutCompanyName.isErrorEnabled = true
            binding.layoutCompanyName.error =
                context?.getString(state.errorCompanyName.resId) ?: ""
        } else {
            binding.layoutCompanyName.isErrorEnabled = false
        }

        if (state.errorDescription?.resId != null) {
            binding.layoutCompanyDescription.isErrorEnabled = true
            binding.layoutCompanyDescription.error =
                context?.getString(state.errorDescription.resId) ?: ""
        } else {
            binding.layoutCompanyDescription.isErrorEnabled = false
        }

        if (state.errorCategory?.resId != null) {
            binding.layoutCompanyCategory.isErrorEnabled = true
            binding.layoutCompanyCategory.error =
                context?.getString(state.errorCategory.resId) ?: ""
        } else {
            binding.layoutCompanyCategory.isErrorEnabled = false
        }
    }

    private fun initDropDownMenuCategories(items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_category_simple, items)
        binding.categoryAutoComplete.setAdapter(adapter)
        binding.categoryAutoComplete.setOnItemClickListener { parent, view, position, id ->
            viewModel.setCategory(items[position])
        }
    }

    private fun initDropDownMenuCities(items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.item_category_simple, items)
        binding.cityAutoComplete.setAdapter(adapter)
        binding.cityAutoComplete.setOnItemClickListener { parent, view, position, id ->
            viewModel.setCity(items[position])
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {


        fun newInstance(): Fragment {

            return CreateCompanyFragment()
        }
    }
}