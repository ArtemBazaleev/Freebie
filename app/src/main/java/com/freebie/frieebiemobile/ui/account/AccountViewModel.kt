package com.freebie.frieebiemobile.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.login.GoogleAuth
import com.freebie.frieebiemobile.login.domain.AuthGoogleUseCase
import com.freebie.frieebiemobile.login.domain.AuthStatus
import com.freebie.frieebiemobile.login.domain.AuthStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authStatusUseCase: AuthStatusUseCase
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }

    val text: LiveData<String> = _text

    init {
        viewModelScope.launch(Dispatchers.Default) {
            when(authStatusUseCase.getAuthStatus()) {
                AuthStatus.AUTHED -> _text.postValue("User authed")
                AuthStatus.NOT_AUTHED -> _text.postValue("User not authed")
            }
        }
    }

}