package com.th3pl4gu3.locky.ui.main.login.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.repository.database.FirebaseUserLiveData
import com.th3pl4gu3.locky.repository.database.UserDao
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState
import com.th3pl4gu3.locky.ui.main.utils.Constants
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

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

            //Start a session
            startSession(user)
        }
    }

    internal fun startSession(user: User) {
        //Store user object in shared preferences
        LocalStorageManager.with(getApplication())
        LocalStorageManager.put(Constants.KEY_USER_ACCOUNT, user)
    }

    private suspend fun save(user: User) {
        withContext(Dispatchers.IO) {
            UserDao().save(user)
        }
    }
}