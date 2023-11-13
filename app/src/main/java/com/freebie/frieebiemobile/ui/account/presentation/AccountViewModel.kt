package com.freebie.frieebiemobile.ui.account.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.login.domain.AuthStatus
import com.freebie.frieebiemobile.login.domain.AuthStatusUseCase
import com.freebie.frieebiemobile.ui.account.domain.GetAccountInfoUseCase
import com.freebie.frieebiemobile.ui.account.domain.OwnUserProfileUseCase
import com.freebie.frieebiemobile.ui.account.domain.model.AccountInfoModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountState
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel
import com.freebie.frieebiemobile.ui.account.presentation.model.UserUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authStatusUseCase: AuthStatusUseCase,
    private val ownUserProfileUseCase: OwnUserProfileUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val mapper: AccountUIMapper
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState(null, emptyList()))
    val state: Flow<AccountState>
        get() = _state

    private val accountInfoJob: Job? = null

    init {
        observeOwnUserProfile()
        requestAccountData()
    }

    fun refresh() {
        if (accountInfoJob?.isActive == true) return
        requestAccountData()
    }

    private fun requestAccountData() {
        viewModelScope.launch {
            val isAuthorized = authStatusUseCase.getAuthStatus() == AuthStatus.AUTHED
            if (isAuthorized) {
                val result = getAccountInfoUseCase.getAccountInfo()
                if (result.isSuccess) {
                    val accountInfo = result.getOrThrow()
                    val sections = mapper.mapAccountInfo(accountInfo, true)
                    _state.emit(
                        AccountState(
                            ownProfile = _state.value.ownProfile,
                            accountUI = sections,
                            isRefreshing = false
                        )
                    )
                } else {
                    _state.emit(
                        AccountState(
                            ownProfile = _state.value.ownProfile,
                            accountUI = _state.value.accountUI,
                            isRefreshing = false
                        )
                    )
                }
            } else {
                _state.emit(
                    AccountState(
                        ownProfile = _state.value.ownProfile,
                        accountUI = mapper.mapAccountInfo(
                            AccountInfoModel(
                                balance = 0.0,
                                coupons = emptyList(),
                                companies = emptyList()
                            ),
                            isAuthorized = false
                        )
                    )
                )
            }
        }
    }

    private fun observeOwnUserProfile() {
        viewModelScope.launch {
            ownUserProfileUseCase.getOwnUserProfileFlow().collect { profile ->
                if (_state.value.ownProfile == null && profile != null) {
                    requestAccountData()
                }
                _state.emit(
                    AccountState(
                        ownProfile = UserUiModel(
                            name = profile.firstName,
                            lastName = profile.lastName,
                            avatar = profile.avatar
                        ),
                        accountUI = _state.value.accountUI
                    ),
                )
            }
        }
    }

    fun onCouponGroupClicked(model: CouponGroupUiModel) {
//        val newGroup = mutableListOf<CouponGroupUiModel>()
//        _state.value.couponTypes.forEach { group ->
//            if (group.groupId == model.groupId) {
//                newGroup.add(group.copy(isActive = true))
//            } else {
//                newGroup.add(group.copy(isActive = false))
//            }
//        }
//        _state.tryEmit(_state.value.copy(couponTypes = newGroup))
    }
}