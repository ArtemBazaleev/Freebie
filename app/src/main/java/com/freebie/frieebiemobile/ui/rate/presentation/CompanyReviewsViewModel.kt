package com.freebie.frieebiemobile.ui.rate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.ui.rate.presentation.mapper.RateUiMapper
import com.freebie.frieebiemobile.ui.rate.presentation.model.CompanyReviewUIState
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateUIModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.UserRateUiModel
import com.freebie.frieebiemobile.ui.rate.presentation.paging.ReviewPagingHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CompanyReviewsViewModel @Inject constructor(
    private val pagingHelper: ReviewPagingHelper,
    private val mapper: RateUiMapper
) : ViewModel() {

    private val _state = MutableStateFlow(CompanyReviewUIState(emptyList()))
    val state: Flow<CompanyReviewUIState>
        get() = _state

    fun initViewModel(companyId: String) {
        pagingHelper.init(viewModelScope, companyId) { reviews ->
            _state.tryEmit(
                CompanyReviewUIState(
                    mutableListOf<RateUIModel>().apply {
                        addAll(_state.value.reviewList)
                        reviews.forEach { rate ->
                            addAll(mapper.mapToUserAndCompanyReply(rate))
                        }
                    }
                )
            )
        }
        pagingHelper.loadAfter()
    }

    fun getPagingCallback() = pagingHelper

}