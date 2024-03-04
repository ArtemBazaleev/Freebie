package com.freebie.frieebiemobile.ui.company.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.databinding.FragmentCreateCompanyBinding
import dagger.hilt.android.AndroidEntryPoint

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
        initDropDownMenu()
    }

    private fun initDropDownMenu() {
        val items = listOf("Food", "Cars", "Kids", "Dj Kirill")
        val adapter = ArrayAdapter(requireContext(), R.layout.item_category_simple, items)
        binding.categoryAutoComplete.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}