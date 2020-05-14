package com.th3pl4gu3.locky.ui.main.login.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.databinding.ActivityLoginBinding
import com.th3pl4gu3.locky.ui.main.main.MainActivity
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState
import com.th3pl4gu3.locky.ui.main.utils.openActivity
import com.th3pl4gu3.locky.ui.main.utils.toast


class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding
    private lateinit var _viewModel: LoginViewModel

    companion object {
        const val SIGN_IN_RESULT_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        _viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding.lifecycleOwner = this

        _binding.ButtonStarted.setOnClickListener {
            launchEmailAuth()
        }

        /* Observe if the user was already authenticated in Firebase Auth*/
        observeAuthenticationState()

        /* Observe the fetched user from local firebase database */
        observeUser()

        /* Observe toast event for any errors*/
        observeNewUserTriggerEvent()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> toast(getString(R.string.message_user_signin_cancelled))
                Activity.RESULT_OK -> return
                else -> toast(
                    getString(
                        R.string.error_internal_code_1,
                        IdpResponse.fromResultIntent(data)?.error?.errorCode?.toString()
                    )
                )
            }
        }
    }

    private fun launchEmailAuth() {
        /** Give users the option to sign in / register with their email
         *If users choose to register with their email,
         *they will need to create a password as well
         * The user also has option to sign in using Google OAuth
         **/
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        /** Create and launch sign-in intent.
         *We listen to the response of this activity with the
         *SIGN_IN_RESULT_CODE code
         **/
        val customLayout: AuthMethodPickerLayout =
            AuthMethodPickerLayout.Builder(R.layout.custom_layout_login)
                .setEmailButtonId(R.id.Button_Email)
                .setGoogleButtonId(R.id.FAB_Google)
                .build()

        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(
                    providers
                ).setAuthMethodPickerLayout(customLayout)
                .setTheme(R.style.Locky_Theme_Launcher_FirebaseUI)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }

    private fun mergeDatabaseUserWithAuthUser(dbUser: User) = User.getInstance().apply {
        dateJoined = dbUser.dateJoined
        accountType = dbUser.accountType
        accountStatus = dbUser.accountStatus
    }

    private fun observeUser() {
        _viewModel.user.observe(this, Observer { user ->
            if (user != null) {
                /*
                * If the user was fetched successfully
                * Create a session if the session doesn't exist already
                */
                _viewModel.createSession(mergeDatabaseUserWithAuthUser(user))

                /* Navigate the user to the main screen */
                navigateToMain()
            }
        })
    }

    private fun observeNewUserTriggerEvent() {
        _viewModel.newUserTrigger.observe(this, Observer {
            when (it) {
                LoginViewModel.UserTrigger.NEW_USER -> createNewUserAndNavigateToMain(User.getInstance())
                LoginViewModel.UserTrigger.EXISTING_USER -> {
                }
                LoginViewModel.UserTrigger.INIT -> {
                }
                else -> {
                }
            }
        })
    }

    private fun observeAuthenticationState() {
        _viewModel.authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                AuthenticationState.AUTHENTICATED -> {
                    login()
                }

                else -> return@Observer
            }
        })
    }

    private fun login() {
        /* Verify if it is an existing user*/
        _viewModel.returningUser(User.getInstance())
    }

    private fun createNewUserAndNavigateToMain(user: User) {
        _viewModel.createUser(user)
        navigateToMain()
    }

    private fun navigateToMain() {
        openActivity(MainActivity::class.java)
        finish()
    }
}
