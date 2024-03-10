package com.freebie.frieebiemobile.ui.company.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.databinding.FragmentCreateCompanyBinding
import com.freebie.frieebiemobile.network.DEFAULT_LOCALE
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyCreationUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale

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
        binding.createCompany.setOnClickListener {
            viewModel.addLocale(
                langCode = resources.configuration.locales[0].language ?: DEFAULT_LOCALE,
                description = binding.etCompanyDescription.text.toString(),
                name = binding.etCompanyName.text.toString()
            )
            viewModel.createCompany()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect(::handleState)
            }
        }
    }


    private fun handleState(state: CompanyCreationUiModel) {
        initDropDownMenuCategories(state.categories.map { it.name })
        initDropDownMenuCities(state.cities)
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
}