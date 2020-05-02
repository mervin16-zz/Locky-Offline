package com.th3pl4gu3.locky.ui.main.main

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.th3pl4gu3.locky.repository.database.FirebaseUserLiveData
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState

class MainActivityViewModel : ViewModel() {

    val authenticationState = Transformations.map(FirebaseUserLiveData()) { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}