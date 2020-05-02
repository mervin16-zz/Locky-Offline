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
import com.th3pl4gu3.locky.core.User
import com.th3pl4gu3.locky.databinding.ActivityLoginBinding
import com.th3pl4gu3.locky.ui.main.main.MainActivity
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_USERS


class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding
    private lateinit var _viewModel: LoginViewModel
    private lateinit var _users: List<User>

    companion object {
        const val SIGN_IN_RESULT_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        _viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding.lifecycleOwner = this

        _users = intent.getParcelableArrayListExtra(KEY_USERS) ?: ArrayList()

        _binding.ButtonStarted.setOnClickListener {
            launchEmailAuth()
        }

        observeAuthenticationState()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> toast(getString(R.string.message_user_signin_cancelled))
                Activity.RESULT_OK -> return
                else -> toast(
                    getString(
                        R.string.error_internal_code_4,
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
        /**
         * Here we check if the firebase auth matches the list of users returned from the previous screen
         * if a match is found,
         * it mean the user already exists.
         * if not, we need to create the user.
         **/

        val user = User.getInstance()
        _users.forEach {
            if (it.email == user.email) {
                /**
                 * Since we already have the user,
                 * we just need to start a session and redirects him/her to the main screen
                 **/
                startSession(it)
                navigateToMain()
                return
            }
        }

        /**
         * If the code reaches here,
         * it means that no user was found in list
         * this means that we need to create the user
         * we create the user in the background
         * then we start a session and redirects the user to the main screen
         **/
        _viewModel.createUser(user)
        startSession(user)
        navigateToMain()
    }

    private fun startSession(user: User) {
        //Store user object in shared preferences
        LocalStorageManager.with(application)
        LocalStorageManager.put(Constants.KEY_USER_ACCOUNT, user)
    }


    private fun navigateToMain() {
        openActivity(MainActivity::class.java)
        finish()
    }
}
