package com.th3pl4gu3.locky.ui.main.login.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.ui.main.login.login.LoginActivity
import com.th3pl4gu3.locky.ui.main.main.MainActivity
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState
import com.th3pl4gu3.locky.ui.main.utils.openActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var _viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        /* Observe if the user was already authenticated in Firebase Auth*/
        observeAuthenticationState()

        /* Observe the fetched user from local firebase database */
        observeUser()

        /* Observe toast event for any errors*/
        observeNewUserTriggerEvent()
    }

    private fun observeNewUserTriggerEvent() {
        _viewModel.newUserTrigger.observe(this, Observer {
            if (it) {
                /*
                * If it returns true,
                * it means it is a new user
                * the user has somehow stayed logged in firebase Auth
                * we then need to clear the firebase auth session and redirect user to login screen
                */
                _viewModel.resetNewUserTrigger()
                clearFirebaseAuthSession()
                navigateToLogin()
            }
        })
    }

    private fun observeUser() {
        _viewModel.user.observe(this, Observer { user ->
            if (user != null) {

                /*
                * If the user was fetched successfully
                * Create a session if the session doesn't exist already
                */
                _viewModel.createSessionIfNoPresent(mergeDatabaseUserWithAuthUser(user))

                /* Navigate the user to the main screen */
                navigateToMain()
            }
        })
    }

    private fun mergeDatabaseUserWithAuthUser(dbUser: User) = User.getInstance().apply {
        dateJoined = dbUser.dateJoined
        accountType = dbUser.accountType
        accountStatus = dbUser.accountStatus
    }

    private fun observeAuthenticationState() {
        _viewModel.authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                AuthenticationState.AUTHENTICATED -> {
                    /*
                     * If the user has already been authenticated in Firebase Auth
                     * Check if it is a returning User.
                     */
                    val user = User.getInstance()
                    _viewModel.returningUser(user)
                }

                AuthenticationState.UNAUTHENTICATED -> {
                    /**
                     * If user is unauthenticated,
                     * redirect to login screen
                     **/
                    navigateToLogin()
                }

                else -> return@Observer
            }
        })
    }

    private fun clearFirebaseAuthSession() {
        AuthUI.getInstance().signOut(this)
    }

    private fun navigateToMain() {
        openActivity(MainActivity::class.java)
        finish()
    }

    private fun navigateToLogin() {
        openActivity(LoginActivity::class.java)
        finish()
    }
}
