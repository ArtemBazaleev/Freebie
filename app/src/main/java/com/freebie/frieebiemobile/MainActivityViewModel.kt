package com.freebie.frieebiemobile

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freebie.frieebiemobile.login.domain.AuthGoogleUseCase
import com.freebie.frieebiemobile.network.socket.SocketIOClient
import com.freebie.frieebiemobile.network.socket.WebSocketClient
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authGoogleUseCase: AuthGoogleUseCase,
    //private val webSocketClient: SocketIOClient
) : ViewModel(), LifecycleObserver {

    init {
        viewModelScope.launch {
            Log.d("MainActivityViewModel", "webSocketClient.initWebSocket() ")
            //webSocketClient.initWebSocket()
        }
    }

    fun credentialsReceived(credential: SignInCredential) {
        viewModelScope.launch {
            val profile = authGoogleUseCase.auth(credential)
            Log.d("MainActivityViewModel", "profile = ${profile.getOrNull()}")
        }
    }
}