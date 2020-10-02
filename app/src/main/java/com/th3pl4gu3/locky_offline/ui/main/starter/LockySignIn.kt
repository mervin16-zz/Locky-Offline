package com.th3pl4gu3.locky_offline.ui.main.starter

import android.app.Application
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.others.User
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_USER_ACCOUNT

class LockySignIn(private val application: Application) {

    private lateinit var _googleSignInClient: GoogleSignInClient

    /* Mutable Live data */
    private val _launchSignInSecurityChecks = MutableLiveData(false)
    private val _proceedToLogin = MutableLiveData<User>()
    private val _showGetStartedButton = MutableLiveData(false)
    private val _showGoogleButton = MutableLiveData(false)
    private val _message = MutableLiveData<String>()

    /* Live Data */
    internal val launchSignInSecurityChecks: LiveData<Boolean> get() = _launchSignInSecurityChecks
    internal val proceedToLogin: LiveData<User> get() = _proceedToLogin
    internal val showGetStartedButton: LiveData<Boolean> get() = _showGetStartedButton
    internal val showGoogleButton: LiveData<Boolean> get() = _showGoogleButton
    internal val message: LiveData<String> get() = _message

    /* Properties */
    internal val googleSignInClient get() = _googleSignInClient

    internal fun buildGoogleSignInClient(activity: FragmentActivity) {
        // Configure sign-in to request the user's ID, email address and profile picture
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        // Build a GoogleSignInClient with the options specified by gso.
        _googleSignInClient = GoogleSignIn.getClient(
            activity, GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        )
    }

    internal fun isUserSignedIn() {
        /*
        * We get the account from Google.
        * If it returns null, it means the user was not signed in
        * Else, the user has already been signed in and we can redirect the user to main
        */
        with(GoogleSignIn.getLastSignedInAccount(application.applicationContext)) {
            when {
                this != null && isUserSavedInSession() -> {
                    _launchSignInSecurityChecks.value = true
                }
                this != null -> {
                    if (isAccountValid(this)) {
                        _proceedToLogin.value = generateUser(this)
                    } else {
                        googleSignOut()
                    }
                }
                else -> {
                    _showGetStartedButton.value = true
                }
            }
        }
    }

    internal fun handleSignInResult(data: Intent?) {
        try {
            with(GoogleSignIn.getSignedInAccountFromIntent(data)
                .getResult(ApiException::class.java)!!) {
                if (isAccountValid(this)) {
                    _proceedToLogin.value = generateUser(this)
                } else {
                    googleSignOut()
                }
            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                GoogleSignInStatusCodes.NETWORK_ERROR -> _message.value =
                    application.getString(R.string.message_internet_connection_unavailable)
                GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> _message.value =
                    application.getString(R.string.message_user_signin_cancelled)
                GoogleSignInStatusCodes.INTERNAL_ERROR -> _message.value = e.message.toString()
                else -> _message.value =
                    application.getString(R.string.error_internal_code_1, e.message.toString())
            }
        }

        _showGoogleButton.value = true
    }

    /*
    * Inaccessible functions
    */
    private fun googleSignOut() {
        /* Log the user out and clear session*/
        _googleSignInClient.signOut()

        _showGetStartedButton.value = true
    }

    private fun isUserSavedInSession(): Boolean = with(LocalStorageManager) {
        /* Returns boolean on whether user already exists or not.*/
        withLogin(application)
        return exists(KEY_USER_ACCOUNT)
    }

    private fun isAccountValid(account: GoogleSignInAccount) =
        account.displayName != null && account.email != null

    private fun generateUser(account: GoogleSignInAccount) = User.getInstance(
        account.displayName!!,
        account.email!!,
        account.photoUrl.toString()
    )
}