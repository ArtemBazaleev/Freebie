package com.freebie.frieebiemobile.ui.account.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.login.domain.AuthStatus
import com.freebie.frieebiemobile.login.domain.AuthStatusUseCase
import com.freebie.frieebiemobile.login.domain.LogoutUseCase
import com.freebie.frieebiemobile.network.NoInternetException
import com.freebie.frieebiemobile.ui.account.data.storage.UserProfileStorage
import com.freebie.frieebiemobile.ui.account.data.storage.UserProfileStorageImpl
import com.freebie.frieebiemobile.ui.account.domain.GetAccountInfoUseCase
import com.freebie.frieebiemobile.ui.account.domain.OwnUserProfileUseCase
import com.freebie.frieebiemobile.ui.account.domain.model.AccountInfoModel
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountState
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupUiModel
import com.freebie.frieebiemobile.ui.account.presentation.model.CouponGroupsUIModel
import com.freebie.frieebiemobile.ui.account.presentation.model.UserUiModel
import com.freebie.frieebiemobile.ui.feed.models.PlaceHolderInfo
import com.freebie.frieebiemobile.ui.utils.PlaceHolderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authStatusUseCase: AuthStatusUseCase,
    private val ownUserProfileUseCase: OwnUserProfileUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val mapper: AccountUIMapper
) : ViewModel() {

    private val isPlaceHolderAnimationShown = AtomicBoolean(false)
    private val _state = MutableStateFlow(AccountState(null, emptyList()))

    @Volatile
    private var accountInfo: AccountInfoModel? = null

    val state: Flow<AccountState>
        get() = _state

    private val accountInfoJob: Job? = null
    private var logoutJob: Job? = null

    init {
        Log.d("AccountViewModel", "init vm")
        observeOwnUserProfile()
        requestAccountData()
    }

    fun refresh() {
        if (accountInfoJob?.isActive == true) return
        requestAccountData()// todo update job
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
            this.accountInfo = accountInfo
            val sections = mapper.mapAccountInfo(accountInfo, isAuthorized = true)
            emitAccountState(accountUI = sections, isRefreshing = false, isAuthed = true)
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
        delay(DELAY_UPDATE_REFRESH)
        emitAccountState(
            accountUI = mapper.mapAccountInfo(
                AccountInfoModel(
                    balance = 0.0,
                    coupons = emptyList(),
                    companies = emptyList()
                ),
                isAuthorized = false
            ),
            isRefreshing = false,
            isAuthed = false
        )
    }

    private suspend fun emitAccountState(
        ownProfile: UserUiModel? = null,
        accountUI: List<AccountUIModel>? = null,
        isRefreshing: Boolean? = null,
        placeholder: PlaceHolderInfo? = null,
        isAuthed: Boolean? = null
    ) = _state.emit(
        AccountState(
            ownProfile = ownProfile ?: _state.value.ownProfile,
            accountUI = accountUI ?: _state.value.accountUI,
            isRefreshing = isRefreshing ?: _state.value.isRefreshing,
            placeholder = placeholder ?: _state.value.placeholder,
            isAuthed = isAuthed ?: _state.value.isAuthed
        ).apply {
            Log.d("AccountViewModel", "emitAccountState = $this")
        }
    )


    private fun observeOwnUserProfile() {
        viewModelScope.launch {
            ownUserProfileUseCase.getOwnUserProfileFlow().collect { profile ->
                Log.d("observeOwnUserProfile", "profile = $profile")
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
        Log.d("AccountViewModel", "model = $model")
        accountInfo?.let { accountInfoModel ->
            val newList = mapper.mapAccountInfo(accountInfoModel, true, model.groupId)
            viewModelScope.launch { emitAccountState(accountUI = newList) }
        }

    }

    fun logout() {
        if (logoutJob?.isActive == true) return
        logoutJob = viewModelScope.launch {
            logoutUseCase.logout().onSuccess { success ->
                if (success) {
                    _state.emit(_state.value.copy(ownProfile = null))
                    refresh()
                }
            }
        }
    }

    companion object {
        private const val DELAY_UPDATE_REFRESH = 100L
    }
}