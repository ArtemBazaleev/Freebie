package com.freebie.frieebiemobile.ui.coupon.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.ui.coupon.domain.GetCouponDetailsUseCase
import com.freebie.frieebiemobile.ui.coupon.presentation.mapper.CouponDetailsUIMapper
import com.freebie.frieebiemobile.ui.coupon.presentation.model.CouponDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CouponDetailsViewModel @Inject constructor(
    private val getCouponDetailsUseCase: GetCouponDetailsUseCase,
    private val mapper: CouponDetailsUIMapper
) : ViewModel() {

    private val _state =
        MutableStateFlow(CouponDetailsState(null)) //add to constructor initial data

    val state: Flow<CouponDetailsState>
        get() = _state

    fun init(couponId: String) {
        requestCouponDetails(couponId)
    }

    private fun requestCouponDetails(couponId: String) {
        viewModelScope.launch {
            runCatching {
                getCouponDetailsUseCase.getCouponDetails(couponId).getOrThrow()
            }.onSuccess { details ->
                _state.emit(
                    CouponDetailsState(
                        couponDetails = mapper.map(details)
                    )
                )
            }.onFailure {
                Log.d("CouponDetailsViewModel", "requestCouponDetails failed e = $it")
            }

        }
    }
}