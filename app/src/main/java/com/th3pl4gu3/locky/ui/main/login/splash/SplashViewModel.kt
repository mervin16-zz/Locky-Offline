package com.th3pl4gu3.locky.ui.main.login.splash

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.repository.database.FirebaseUserLiveData
import com.th3pl4gu3.locky.repository.database.UserDao
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private var _newUserTrigger = MutableLiveData(false)
    private var _user = MediatorLiveData<User>()

    val user: LiveData<User>
        get() = _user

    val newUserTrigger: LiveData<Boolean>
        get() = _newUserTrigger

    internal fun resetNewUserTrigger() {
        _newUserTrigger.value = false
    }

    internal val authenticationState = Transformations.map(FirebaseUserLiveData()) { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    internal fun returningUser(user: User) = _user.addSource(UserDao().getAll(user.email)) {
        _user.value = decomposeDataSnapshots(it)
    }

    internal fun createSessionIfNoPresent(user: User) {
        //Store user object in shared preferences
        LocalStorageManager.with(getApplication())
        if (!LocalStorageManager.exists(KEY_USER_ACCOUNT)) LocalStorageManager.put(
            KEY_USER_ACCOUNT,
            user
        )
    }

    private fun decomposeDataSnapshots(snapshot: DataSnapshot?): User? {
        /**
         * Function to transform data snapshots into a user
         **/
        var user: User? = null

        snapshot?.children?.forEach { postSnapshot ->
            postSnapshot.getValue<User>()?.let {
                user = it
            }
        }

        return user.also {
            if (it == null) {
                _newUserTrigger.value = true
            }
        }
    }

}