package com.freebie.frieebiemobile

import android.util.Log
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
            val profile = authGoogleUseCase.auth(credential)
            Log.d("MainActivityViewModel", "profile = ${profile.getOrNull()}")
        }
    }
}