package com.th3pl4gu3.locky_offline.ui.main.starter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.core.others.User
import com.th3pl4gu3.locky_offline.repository.database.repositories.UserRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.SettingsManager
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.merge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StarterViewModel(application: Application) : AndroidViewModel(application) {

    /* Private Variables */
    private val _buttonVisibility = MutableLiveData(0)
    private val _signInCompletion = MutableLiveData(false)

    /* Public Variables */
    val canNavigateToMainScreen = MutableLiveData(false)

    /* Properties */
    val buttonVisibility: LiveData<Int>
        get() = _buttonVisibility

    val isSignInComplete: LiveData<Boolean>
        get() = _signInCompletion

    /*
    * Accessible Functions
    */
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
            var fetchedUser = fetchUser(user.email)

            fetchedUser = if (fetchedUser == null) {
                save(user)
                /*
                * Assigned saved user to fetch user
                * We do this to always get an updated version of the user object
                * to save in the session
                */
                user
            } else {
                /*
                * We merge fetched user to get
                * an updated data
                */
                user.merge(fetchedUser)
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

    internal fun isUserSavedInSession(): Boolean = with(LocalStorageManager) {
        /* Returns boolean on whether user already exists or not.*/
        withLogin(getApplication())
        return exists(KEY_USER_ACCOUNT)
    }

    internal fun isMasterPasswordEnabled(): Boolean =
        SettingsManager(getApplication()).isMasterPasswordEnabled()

    internal fun fetchMasterPassword() = SettingsManager(getApplication()).getMasterPassword()

    internal fun isBiometricsEnabled(): Boolean =
        SettingsManager(getApplication()).isBiometricsEnabled()

    /*
    * In-Accessible Functions
    */
    private fun saveToSession(user: User) = with(LocalStorageManager) {
        withLogin(getApplication())
        put(KEY_USER_ACCOUNT, user)
    }

    private suspend fun fetchUser(key: String): User? {
        /* Fetch the data from Database */
        var user: User? = null
        withContext(Dispatchers.IO) {
            user = UserRepository.getInstance(getApplication()).get(key)
        }
        return user
    }

    /* Saves the user in database */
    private suspend fun save(user: User) = withContext(Dispatchers.IO) {
        UserRepository.getInstance(getApplication()).insert(user)
    }
}