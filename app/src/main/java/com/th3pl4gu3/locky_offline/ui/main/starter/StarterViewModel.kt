package com.th3pl4gu3.locky_offline.ui.main.starter

import android.app.Application
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.core.others.User
import com.th3pl4gu3.locky_offline.repository.database.repositories.UserRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.merge
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.SettingsManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_USER_ACCOUNT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StarterViewModel(application: Application) : AndroidViewModel(application) {

    /* Private Variables */
    private val _buttonVisibility = MutableLiveData(0)
    private val _signInCompletion = MutableLiveData(false)
    private val _lockySignIn = LockySignIn(application)
    private val _lockyLoginSecurity = LockyLoginSecurity(application)

    /* Public Properties */
    val buttonVisibility: LiveData<Int> get() = _buttonVisibility

    val isSignInComplete: LiveData<Boolean> get() = _signInCompletion

    /* Locky Sign In Live Data */
    val showGetStartedButton = _lockySignIn.showGetStartedButton

    val showGoogleButton = _lockySignIn.showGoogleButton

    val launchSignInSecurityChecks = _lockySignIn.launchSignInSecurityChecks

    val proceedToLogin = _lockySignIn.proceedToLogin

    val message = _lockySignIn.message

    /* Locky Login Security Live Data */
    val launchBiometrics = _lockyLoginSecurity.launchBiometrics

    val launchBiometricsDialog = _lockyLoginSecurity.launchBiometricsDialog

    val launchMasterPassword = _lockyLoginSecurity.launchMasterPassword

    val hasNoSecurityEnabled = _lockyLoginSecurity.hasNoSecurityEnabled

    /*
    * Accessible Functions
    */
    internal fun launchLoginSecurityCheck() {
        _lockyLoginSecurity.launchVerification()
    }

    internal fun requireGoogleSignInClient() = _lockySignIn.googleSignInClient

    internal fun setupSignIn(activity: FragmentActivity) {
        _lockySignIn.buildGoogleSignInClient(activity)
    }

    internal fun isUserSignedIn() {
        _lockySignIn.isUserSignedIn()
    }

    internal fun handleSignInResult(data: Intent?) {
        _lockySignIn.handleSignInResult(data)
    }

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

    internal fun isMasterPasswordEnabled(): Boolean =
        SettingsManager(getApplication()).isMasterPasswordEnabled()

    internal fun fetchMasterPassword() = SettingsManager(getApplication()).getMasterPassword()

    /*
    * In-Accessible Functions
    */
    private fun saveToSession(user: User) = with(LocalStorageManager) {
        withLogin(getApplication())
        put(KEY_USER_ACCOUNT, user)
    }

    private suspend fun fetchUser(key: String): User? {
        /* Fetch the data from Database */
        var user: User?
        withContext(Dispatchers.IO) {
            user = UserRepository.getInstance(getApplication()).get(key)
        }
        return user
    }

    private suspend fun save(user: User) = withContext(Dispatchers.IO) {
        UserRepository.getInstance(getApplication()).insert(user)
    }
}