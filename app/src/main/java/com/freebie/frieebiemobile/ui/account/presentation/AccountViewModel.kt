package com.freebie.frieebiemobile.ui.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.login.domain.AuthStatus
import com.freebie.frieebiemobile.login.domain.AuthStatusUseCase
import com.freebie.frieebiemobile.network.NoInternetException
import com.freebie.frieebiemobile.ui.account.data.storage.UserProfileStorage
import com.freebie.frieebiemobile.ui.account.data.storage.UserProfileStorageImpl
import com.freebie.frieebiemobile.ui.account.domain.GetAccountInfoUseCase
import com.freebie.frieebiemobile.ui.account.domain.OwnUserProfileUseCase
import com.freebie.frieebiemobile.ui.account.domain.model.AccountInfoModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountState
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel
import com.freebie.frieebiemobile.ui.account.presentation.model.UserUiModel
import com.freebie.frieebiemobile.ui.feed.models.PlaceHolderInfo
import com.freebie.frieebiemobile.ui.utils.PlaceHolderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authStatusUseCase: AuthStatusUseCase,
    private val ownUserProfileUseCase: OwnUserProfileUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val mapper: AccountUIMapper
) : ViewModel() {

    private val isPlaceHolderAnimationShown = AtomicBoolean(false)
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
            emitAccountState(isRefreshing = true)
            if (isAuthorized) {
                handleAccountResult(getAccountInfoUseCase.getAccountInfo())
            } else {
                emitUnAuthorizedState()
            }
        }
    }

    private suspend fun handleAccountResult(result: Result<AccountInfoModel>) {
        try {
            val accountInfo = result.getOrThrow()
            val sections = mapper.mapAccountInfo(accountInfo, isAuthorized = true)
            emitAccountState(accountUI = sections, isRefreshing = false)
        } catch (e: NoInternetException) {
            emitAccountState(
                isRefreshing = false,
                placeholder = PlaceHolderInfo(
                    state = PlaceHolderState.NoInternet,
                    needToAnimate = isPlaceHolderAnimationShown.get().not()
                ).apply { isPlaceHolderAnimationShown.set(true) }
            )
        } catch (e: Exception) {
            emitAccountState(
                isRefreshing = false,
                placeholder = PlaceHolderInfo(
                    state = PlaceHolderState.SomethingWentWrong,
                    needToAnimate = isPlaceHolderAnimationShown.get().not()
                ).apply { isPlaceHolderAnimationShown.set(true) }
            )
        }
    }

    private suspend fun emitUnAuthorizedState() {
        emitAccountState(
            accountUI = mapper.mapAccountInfo(
                AccountInfoModel(
                    balance = 0.0,
                    coupons = emptyList(),
                    companies = emptyList()
                ),
                isAuthorized = false
            ),
            isRefreshing = false
        )
    }

    private suspend fun emitAccountState(
        ownProfile: UserUiModel? = null,
        accountUI: List<AccountUIModel>? = null,
        isRefreshing: Boolean? = null,
        placeholder: PlaceHolderInfo? = null
    ) = _state.emit(
        AccountState(
            ownProfile = ownProfile ?: _state.value.ownProfile,
            accountUI = accountUI ?: _state.value.accountUI,
            isRefreshing = isRefreshing ?: _state.value.isRefreshing,
            placeholder = placeholder ?: _state.value.placeholder
        )
    )


    private fun observeOwnUserProfile() {
        viewModelScope.launch {
            ownUserProfileUseCase.getOwnUserProfileFlow().collect { profile ->
                if (_state.value.ownProfile == null) {
                    requestAccountData()
                }
                emitAccountState(
                    ownProfile = UserUiModel(
                        name = profile.firstName,
                        lastName = profile.lastName,
                        avatar = profile.avatar
                    )
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