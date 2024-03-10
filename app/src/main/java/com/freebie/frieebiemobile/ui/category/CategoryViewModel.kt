package com.freebie.frieebiemobile.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.network.NoInternetException
import com.freebie.frieebiemobile.ui.category.domain.usecase.GetCategoryRepositoryUseCase
import com.freebie.frieebiemobile.ui.category.mapper.CategoryMapper
import com.freebie.frieebiemobile.ui.category.model.CategoryState
import com.freebie.frieebiemobile.ui.category.model.ShimmerCategory
import com.freebie.frieebiemobile.ui.feed.models.PlaceHolderInfo
import com.freebie.frieebiemobile.ui.utils.PlaceHolderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoryRepositoryUseCase: GetCategoryRepositoryUseCase,
    private val mapper: CategoryMapper
) : ViewModel() {

    private val _state = MutableStateFlow(
        CategoryState(
            getInitialData(),
            PlaceHolderInfo(true, null)
        )
    )
    val state: Flow<CategoryState> = _state
    private var categoriesJob: Job? = null

    init {
        requestCategories()
    }

    private fun requestCategories() {
        categoriesJob?.cancel()
        categoriesJob = viewModelScope.launch {
            val categoryResult = getCategoryRepositoryUseCase()
            when {
                categoryResult.isSuccess -> {
                    val categories = categoryResult.getOrDefault(emptyList()).map(mapper::mapToUi)
                    if (categories.isEmpty()) emitPlaceholderInfo(PlaceHolderState.NoData)
                    else _state.emit(_state.value.copy(categories = categories))
                }

                categoryResult.exceptionOrNull() is NoInternetException -> {
                    emitPlaceholderInfo(PlaceHolderState.NoInternet)
                }

                else -> {
                    emitPlaceholderInfo(PlaceHolderState.SomethingWentWrong)
                }
            }
        }
    }

    private suspend fun emitPlaceholderInfo(
        placeHolderState: PlaceHolderState?
    ) {
        val categories = _state.value.categories
        val newCategories = if (isShimmerState()) {
            emptyList()
        } else categories
        _state.emit(
            _state.value.copy(
                categories = newCategories,
                placeholder = PlaceHolderInfo(
                    needToAnimate = true,
                    state = placeHolderState
                )
            )
        )
    }

    private fun isShimmerState(): Boolean {
        val categories = _state.value.categories
        return categories.isNotEmpty() && categories[0] == ShimmerCategory
    }

    private fun getInitialData(): List<ShimmerCategory> = mutableListOf<ShimmerCategory>().apply {
        repeat(10) { add(ShimmerCategory) }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isShimmerState() && _state.value.categories.isEmpty()) {
                _state.emit(_state.value.copy(categories = getInitialData()))
            }
            requestCategories()
        }
    }

}