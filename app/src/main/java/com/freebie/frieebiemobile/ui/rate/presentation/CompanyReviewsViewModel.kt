package com.freebie.frieebiemobile.ui.rate.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.ui.rate.domain.usecase.ReplyCommentUseCase
import com.freebie.frieebiemobile.ui.rate.presentation.mapper.RateUiMapper
import com.freebie.frieebiemobile.ui.rate.presentation.model.CompanyReviewUIState
import com.freebie.frieebiemobile.ui.rate.presentation.model.RateUIModel
import com.freebie.frieebiemobile.ui.rate.presentation.model.UserRateUiModel
import com.freebie.frieebiemobile.ui.rate.presentation.paging.ReviewPagingHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyReviewsViewModel @Inject constructor(
    private val pagingHelper: ReviewPagingHelper,
    private val mapper: RateUiMapper,
    private val replyCommentUseCase: ReplyCommentUseCase
) : ViewModel() {

    @Volatile
    private var companyId: String? = null

    @Volatile
    private var canModerate: Boolean? = null

    private val _state = MutableStateFlow(CompanyReviewUIState(emptyList()))

    val state: Flow<CompanyReviewUIState>
        get() = _state

    fun initViewModel(companyId: String, canModerate: Boolean) {
        this.companyId = companyId
        this.canModerate = canModerate
        pagingHelper.init(viewModelScope, companyId) { reviews ->
            _state.tryEmit(
                CompanyReviewUIState(
                    mutableListOf<RateUIModel>().apply {
                        addAll(_state.value.reviewList)
                        reviews.forEach { rate ->
                            addAll(mapper.mapToUserAndCompanyReply(
                                model = rate,
                                canModerate = canModerate
                            ))
                        }
                    }
                )
            )
        }
        pagingHelper.loadAfter()
    }

    fun replyComment(message: String, commentId: String) {
        viewModelScope.launch {
            replyCommentUseCase.replyComment(message, commentId)
                .onSuccess {
                    refresh()
                    Log.d("CompanyReviewsViewModel", "replyComment success $it")
                }.onFailure {
                    Log.e("CompanyReviewsViewModel", "replyComment fail ${it.message}")
                }
        }
    }

    fun refresh() {
        companyId?.let { nonNullId ->
            viewModelScope.launch {
                pagingHelper.clear()
                _state.emit(CompanyReviewUIState(emptyList()))
                initViewModel(nonNullId, canModerate ?: false)
            }
        }
    }

    fun getPagingCallback() = pagingHelper

}