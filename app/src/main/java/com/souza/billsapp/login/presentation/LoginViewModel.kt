package com.souza.billsapp.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.souza.billsapp.login.data.FirebaseUserLiveData

class LoginViewModel : ViewModel() {

        enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData()
        .map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}
