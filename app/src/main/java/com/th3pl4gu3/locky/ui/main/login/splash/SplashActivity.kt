package com.th3pl4gu3.locky.ui.main.login.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.th3pl4gu3.locky.core.User
import com.th3pl4gu3.locky.ui.main.login.login.LoginActivity
import com.th3pl4gu3.locky.ui.main.main.MainActivity
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState
import com.th3pl4gu3.locky.ui.main.utils.Constants
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_USERS
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky.ui.main.utils.openActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var _viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        /**
         * Observe live data to list of all users
         */
        observeUsers()
    }

    private fun observeUsers() {
        _viewModel.users.observe(this, Observer { users ->
            if (users != null) {
                /**
                 * Once we obtain the list, we can start observing
                 * if user has been authenticated or not
                 **/
                /**
                 * Once we obtain the list, we can start observing
                 * if user has been authenticated or not
                 **/
                observeAuthenticationState(users)
            }
        })
    }

    private fun observeAuthenticationState(users: List<User>) {
        _viewModel.authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                AuthenticationState.AUTHENTICATED -> {
                    /**
                     * If the user has already been authenticated
                     * 1. Create an instance of user
                     * 2. Verify if the instance of user is found in lsit of fetched users
                     **/
                    users.forEach {
                        if (it.email == User.getInstance().email) {
                            /**
                             * When the user is found,
                             * start a session
                             * we can then navigate to main screen
                             **/
                            startSession(it)
                            navigateToMain()
                            return@Observer
                        }
                    }

                    /**
                     * If the code reaches here,
                     * it means that no user was found in list
                     * this means that the firebase auth is somehow still active
                     * we need to clear the firebase auth and clear the stored preferences
                     * We then navigate user to login screen
                     **/
                    clearSession()
                    navigateToLogin(users)
                }

                AuthenticationState.UNAUTHENTICATED -> {
                    /**
                     * If user is unauthenticated,
                     * pass the list of users to the login screen for filtration
                     * when the user login
                     **/
                    navigateToLogin(users)
                }

                else -> return@Observer
            }
        })
    }

    private fun navigateToMain() {
        openActivity(MainActivity::class.java)
        finish()
    }

    private fun navigateToLogin(users: List<User>) {
        openActivity(LoginActivity::class.java) {
            putParcelableArrayList(KEY_USERS, ArrayList(users))
        }
        finish()
    }

    private fun startSession(user: User) {
        //Store user object in shared preferences
        LocalStorageManager.with(application)
        LocalStorageManager.put(Constants.KEY_USER_ACCOUNT, user)
    }

    private fun clearSession() {
        //Remove user object in shared preferences
        LocalStorageManager.with(application)
        LocalStorageManager.remove(Constants.KEY_USER_ACCOUNT)
        //Clear firebase auth session
        AuthUI.getInstance().signOut(this)
    }
}
