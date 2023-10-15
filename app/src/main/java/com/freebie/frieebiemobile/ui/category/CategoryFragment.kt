package com.freebie.frieebiemobile.ui.category

import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.freebie.frieebiemobile.R
import com.freebie.frieebiemobile.databinding.FragmentCategoryBinding
import com.freebie.frieebiemobile.ui.category.adapter.CategoryAdapter
import com.freebie.frieebiemobile.ui.category.model.CategoryState
import com.freebie.frieebiemobile.ui.feed.decorator.OffsetDecorator
import com.freebie.frieebiemobile.ui.utils.getNavComponent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val categoryViewModel by viewModels<CategoryViewModel>()
    private var adapter: CategoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        observeData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.refreshLayout.setOnRefreshListener {
            categoryViewModel.refresh()
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun initAdapter() {
        adapter = CategoryAdapter {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                binding.root.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            }
            requireActivity().getNavComponent()?.navigate(R.id.navigation_category_feed)
        }
        binding.rvCategory.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvCategory.adapter = adapter
        binding.rvCategory.addItemDecoration(OffsetDecorator())
    }

    private fun observeData() {
        categoryViewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach(::handleState)
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: CategoryState) {
        if (state.categories.isEmpty() && state.placeholder.state != null) {
            binding.placeholder.visibility = View.VISIBLE
            binding.placeholder.setState(
                state.placeholder.state,
                state.placeholder.needToAnimate
            )
        } else {
            binding.placeholder.visibility = View.GONE
        }
        adapter?.submitList(state.categories)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }
}