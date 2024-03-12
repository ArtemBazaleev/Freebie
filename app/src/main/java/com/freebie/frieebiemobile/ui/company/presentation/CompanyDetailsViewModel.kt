package com.freebie.frieebiemobile.ui.company.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.network.NoInternetException
import com.freebie.frieebiemobile.ui.account.domain.GetOwnLocalProfileUseCase
import com.freebie.frieebiemobile.ui.account.domain.OwnUserProfileUseCase
import com.freebie.frieebiemobile.ui.company.domain.usecase.GetCompanyDetailsUseCase
import com.freebie.frieebiemobile.ui.company.presentation.mapper.CompanyUiMapper
import com.freebie.frieebiemobile.ui.company.presentation.model.CompanyDetailsUiState
import com.freebie.frieebiemobile.ui.company.presentation.model.EMPTY_COMPANY_UI_STATE
import com.freebie.frieebiemobile.ui.company.presentation.paging.BookletsCompanyPagingHelper
import com.freebie.frieebiemobile.ui.company.presentation.paging.CouponsCompanyPagingHelper
import com.freebie.frieebiemobile.ui.coupon.domain.GetCouponsByCompanyUseCase
import com.freebie.frieebiemobile.ui.feed.models.CouponAdapterUiModel
import com.freebie.frieebiemobile.ui.feed.models.CouponUI
import com.freebie.frieebiemobile.ui.feed.models.OfferUI
import com.freebie.frieebiemobile.ui.utils.PaginationCallback
import com.freebie.frieebiemobile.ui.utils.PlaceHolderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyDetailsViewModel @Inject constructor(
    private val getCompanyDetailsUseCase: GetCompanyDetailsUseCase,
    private val couponsPagingHelper: CouponsCompanyPagingHelper,
    private val bookletPagingHelper: BookletsCompanyPagingHelper,
    private val ownProfileUseCase: GetOwnLocalProfileUseCase,
    private val mapper: CompanyUiMapper
): ViewModel() {

    private val _state = MutableStateFlow(EMPTY_COMPANY_UI_STATE)
    private val _placeholderState = MutableStateFlow<PlaceHolderState?>(null)
    private var requestCompanyInfoJob: Job? = null

    val state: Flow<CompanyDetailsUiState>
        get() = _state

    val placeholderState: Flow<PlaceHolderState?>
        get() = _placeholderState

    fun requestCompanyDetails(companyId: String) {
        if (requestCompanyInfoJob?.isActive == true) return
        initPaging(companyId)
        requestCompanyInfoJob = viewModelScope.launch {
            _placeholderState.emit(null)
            val ownProfile = ownProfileUseCase.getOwnLocalProfile()
            getCompanyDetailsUseCase.getCompanyInfo(companyId).onSuccess { companyModel ->
                couponsPagingHelper.clear()
                bookletPagingHelper.clear()
                _state.emit(mapper.map(companyModel, ownProfile?.uid))
                _placeholderState.emit(null)
            }.onFailure {
                delay(100)
                if (it is NoInternetException) _placeholderState.emit(PlaceHolderState.NoInternet)
                else _placeholderState.emit(PlaceHolderState.SomethingWentWrong)
            }
        }
    }

    private fun initPaging(companyId: String) {
        couponsPagingHelper.init(viewModelScope, companyId) { couponUIS ->
            val state = _state.value
            val newCoupons = mutableListOf<CouponAdapterUiModel>().apply {
                addAll(_state.value.coupons)
                addAll(couponUIS)
            }
            _state.tryEmit(state.copy(coupons = newCoupons))
        }

        bookletPagingHelper.init(viewModelScope, companyId) { booklets ->
            val state = _state.value
            val bookletsList = mutableListOf<OfferUI>().apply {
                addAll(_state.value.booklets)
                addAll(booklets)
            }
            _state.tryEmit(state.copy(booklets = bookletsList))
        }
    }

    fun getCouponsPagingCallback() = couponsPagingHelper

    fun getBookletsPagingCallback() = bookletPagingHelper

}