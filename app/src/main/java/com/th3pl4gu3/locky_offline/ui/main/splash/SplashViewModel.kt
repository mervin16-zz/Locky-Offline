package com.th3pl4gu3.locky_offline.ui.main.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.repository.database.repositories.UserRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.merge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val _buttonVisibility = MutableLiveData(0)
    private val _signInCompletion = MutableLiveData(false)

    val buttonVisibility: LiveData<Int>
        get() = _buttonVisibility

    val isSignInComplete: LiveData<Boolean>
        get() = _signInCompletion

    internal fun showLoading() {
        _buttonVisibility.value = 0
    }

    internal fun showGetStartedButton() {
        _buttonVisibility.value = 1
    }

    internal fun showGoogleButton() {
        _buttonVisibility.value = 2
    }

    internal fun login(user: User) {
        /*
        * It checks if user has already been logged in
        * If not, we create the user.
        * After we are sure user has been added in database, we update live data.
        */
        viewModelScope.launch {
            var fetchedUser = fetch(user.email)

            if (fetchedUser == null) {
                save(user)
                /*
                * Assigned saved user to fetch user
                * We do this to always get an updated version of the user object
                * to save in the session
                */
                fetchedUser = user
            } else {
                /*
                * We merge fetched user to get
                * an updated data
                */
                fetchedUser = user.merge(fetchedUser)
            }

            /*
            * We then save the user to session
            */
            saveToSession(fetchedUser)

            /*
            * We mark the sign is as completed
            */
            _signInCompletion.value = true
        }
    }

    internal fun isUserSavedInSession(): Boolean {
        /* Returns boolean on whether user already exists or not.*/
        LocalStorageManager.withLogin(getApplication())
        return LocalStorageManager.exists(KEY_USER_ACCOUNT)
    }

    private fun saveToSession(user: User) {
        //Store user object in shared preferences
        LocalStorageManager.withLogin(getApplication())
        LocalStorageManager.put(KEY_USER_ACCOUNT, user)
    }

    private suspend fun fetch(key: String): User? {
        /* Fetch the data from Database */
        var user: User? = null
        withContext(Dispatchers.IO) {
            user = UserRepository.getInstance(getApplication()).get(key)
        }
        return user
    }

    private suspend fun save(user: User) {
        /* Saves the user in database */
        withContext(Dispatchers.IO) {
            UserRepository.getInstance(getApplication()).insert(user)
        }
    }
}