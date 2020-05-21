package com.th3pl4gu3.locky.ui.main.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.databinding.ActivitySplashBinding
import com.th3pl4gu3.locky.ui.main.main.MainActivity
import com.th3pl4gu3.locky.ui.main.utils.AuthenticationState
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky.ui.main.utils.openActivity
import com.th3pl4gu3.locky.ui.main.utils.toast

class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private var _viewModel: SplashViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    companion object {
        const val SIGN_IN_RESULT_CODE = 1001
        const val TAG = "Splash_Activity_Debug"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        /* Re set the style to the main theme app */
        setTheme(R.style.Locky_Theme)
        super.onCreate(savedInstanceState)
        /* Bind splash screen and instantiate view model
        * Bind view model to layout and set lifecycle owner
        **/
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        _viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        /* Updates the app settings*/
        updateAppSettings()

        /* Listener for Get Started Button */
        listenerForGetStartedButton()

        /* Observer user retrieval event*/
        observeUser()

        /* Observer user authentication event*/
        observeAuthenticationState()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

    private fun observeUser() {
        viewModel.user.observe(this, Observer {
            if (it != null) {
                /*
                * When the user is retrieved successfully
                * We reset the user data in view model
                */
                viewModel.resetUsers()

                /*
                * We then check account status of user
                */
                when (it.accountStatus) {
                    User.AccountStatus.INACTIVE -> toast(getString(R.string.message_user_account_status_inactive))
                    User.AccountStatus.BLOCKED -> toast(getString(R.string.message_user_account_status_blocked))
                    User.AccountStatus.ACTIVE -> {
                        /*
                        * If the account is active
                        * /*TODO(Check trial period)*/
                        * If everything is fine, we navigate to main.
                        */
                        createSessionIfNoPresent(it)
                        navigateToMain()
                    }
                }
            }
        })
    }

    private fun observeAuthenticationState() =
        viewModel.authenticationState.observe(this, Observer { authenticationState ->
            when (authenticationState) {
                AuthenticationState.UNAUTHENTICATED -> {
                    /*
                    * If the user is unauthenticated
                    * We show the get started button by hiding loading
                    * */
                    viewModel.finishLoading()
                }
                AuthenticationState.AUTHENTICATED -> {
                    /*
                    * If the user has already been authenticated,
                    * We first check if an instance of the user object exists and matches the user trying to log in
                    * If it matches, we log in the user directly to bypass overhead loading
                    */
                    if (isUserSessionPresent()) navigateToMain() else login()
                }
                else -> return@Observer
            }
        })

    private fun login() {
        /*
        * Get instance of user and login
        */
        viewModel.login(User.getInstance())
    }

    private fun listenerForGetStartedButton() {
        binding.ButtonStarted.setOnClickListener {
            launchOAuth()
        }
    }

    private fun launchOAuth() {
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
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setAuthMethodPickerLayout(customLayout)
                .setTheme(R.style.Locky_Theme_Launcher_FirebaseUI)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }

    private fun isUserSessionPresent(): Boolean {
        /*
        * Here we check is an instance of the user has already been stored in session
        */
        LocalStorageManager.with(application)

        return if (LocalStorageManager.exists(KEY_USER_ACCOUNT)) {
            /* Is instance exists, we check if it matches the user trying to log in*/
            val firebaseUserInstance = User.getInstance()
            val sessionUserInstance = LocalStorageManager.get<User>(KEY_USER_ACCOUNT)
            /* If it matches, return true, else will return false*/
            firebaseUserInstance.email == sessionUserInstance?.email
        } else {
            /* No isntance exists, therefore we need to proceed with the login process*/
            false
        }
    }

    private fun createSessionIfNoPresent(user: User) {
        /* Store user object in shared preferences if not present already */
        LocalStorageManager.with(application)
        if (!LocalStorageManager.exists(KEY_USER_ACCOUNT)) LocalStorageManager.put(
            KEY_USER_ACCOUNT,
            user
        )
    }

    private fun updateAppSettings() {
        LocalStorageManager.with(application)

        when (LocalStorageManager.get<String>(getString(R.string.settings_key_display_theme))) {
            getString(R.string.settings_value_display_default) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
            getString(R.string.settings_value_display_light) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            getString(R.string.settings_value_display_dark) -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }
    }

    private fun navigateToMain() {
        /* Navigate to main screen */
        openActivity(MainActivity::class.java)
        finish()
    }
}