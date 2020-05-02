package com.th3pl4gu3.locky.ui.main.login.login

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky.core.User
import com.th3pl4gu3.locky.repository.database.FirebaseUserLiveData
import com.th3pl4gu3.locky.repository.database.UserDao
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    internal val authenticationState = Transformations.map(FirebaseUserLiveData()) { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    internal fun createUser(user: User) {
        viewModelScope.launch {
            save(user)
        }
    }

    private suspend fun save(user: User) {
        withContext(Dispatchers.IO) {
            UserDao().save(user)
        }
    }
}