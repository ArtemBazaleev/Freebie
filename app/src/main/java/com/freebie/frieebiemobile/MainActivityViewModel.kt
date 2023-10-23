package com.freebie.frieebiemobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.login.domain.AuthGoogleUseCase
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authGoogleUseCase: AuthGoogleUseCase
) : ViewModel() {

    fun credentialsReceived(credential: SignInCredential) {
        viewModelScope.launch {
            authGoogleUseCase.auth(credential)
        }
    }
}