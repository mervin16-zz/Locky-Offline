package com.th3pl4gu3.locky.ui.main.login.login

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
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

    enum class UserTrigger { NEW_USER, EXISTING_USER, INIT }

    private var _newUserTrigger = MutableLiveData(UserTrigger.INIT)
    private var _user = MediatorLiveData<User>()

    val user: LiveData<User>
        get() = _user

    val newUserTrigger: LiveData<UserTrigger>
        get() = _newUserTrigger

    internal val authenticationState = Transformations.map(FirebaseUserLiveData()) { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    internal fun returningUser(user: User) {
        _user.addSource(UserDao().getAll(user.email)) {
            _user.value = decomposeDataSnapshots(it)
        }
    }

    internal fun createUser(user: User) {
        viewModelScope.launch {
            save(user)

            //Start a session
            createSession(user)
        }
    }

    internal fun createSession(user: User) {
        //Store user object in shared preferences
        LocalStorageManager.with(getApplication())
        LocalStorageManager.put(Constants.KEY_USER_ACCOUNT, user)
    }

    private suspend fun save(user: User) {
        withContext(Dispatchers.IO) {
            UserDao().save(user)
        }
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
                _newUserTrigger.value = UserTrigger.NEW_USER
            } else {
                _newUserTrigger.value = UserTrigger.EXISTING_USER
            }
        }
    }
}