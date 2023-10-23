package com.freebie.frieebiemobile.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.freebie.frieebiemobile.login.GoogleAuth
import com.freebie.frieebiemobile.login.domain.AuthGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

}