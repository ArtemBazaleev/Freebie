package com.freebie.frieebiemobile.ui.account.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.login.domain.AuthStatusUseCase
import com.freebie.frieebiemobile.ui.account.domain.OwnUserProfileUseCase
import com.freebie.frieebiemobile.ui.account.presentation.model.AccountState
import com.freebie.frieebiemobile.ui.account.presentation.model.UserUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authStatusUseCase: AuthStatusUseCase,
    private val ownUserProfileUseCase: OwnUserProfileUseCase
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }

    val text: LiveData<String> = _text

    private val _state = MutableStateFlow(AccountState(null))
    val state: Flow<AccountState>
        get() = _state

    init {

        observeOwnUserProfile()
    }

    private fun observeOwnUserProfile() {
        viewModelScope.launch {
            ownUserProfileUseCase.getOwnUserProfileFlow().collect { profile ->
                _state.emit(
                    AccountState(
                        ownProfile = UserUiModel(
                            name = profile.firstName,
                            lastName = profile.lastName,
                            avatar = profile.avatar
                        )
                    )
                )
                Log.d("AccountViewModel", "observed profile = $profile")
            }
        }
    }
}