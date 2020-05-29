package com.th3pl4gu3.locky_offline.ui.main.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigator
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.databinding.ActivitySplashBinding
import com.th3pl4gu3.locky_offline.ui.main.main.MainActivity
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.openActivity

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun finish() {
        super.finish()

        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
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
                /*when (it.accountStatus) {
                    User.AccountStatus.INACTIVE -> toast(getString(R.string.message_user_account_status_inactive))
                    User.AccountStatus.BLOCKED -> toast(getString(R.string.message_user_account_status_blocked))
                    User.AccountStatus.ACTIVE -> {
                        *//*
                        * If the account is active
                        *
                        * If everything is fine, we navigate to main.
                        *//*
                        createSessionIfNoPresent(it)
                        navigateToMain()
                    }
                }*/
                TODO("Fix")
            }
        })
    }

    private fun login() {
        /*
        * Get instance of user and login
        */
        viewModel.login(User())
    }

    private fun listenerForGetStartedButton() {
        binding.ButtonStarted.setOnClickListener {
            launchOAuth()
        }
    }

    private fun launchOAuth() {

    }

    private fun isUserSessionPresent(): Boolean {
        /*
        * Here we check is an instance of the user has already been stored in session
        */
        LocalStorageManager.with(application)

        return if (LocalStorageManager.exists(KEY_USER_ACCOUNT)) {
            /* Is instance exists, we check if it matches the user trying to log in*/
            val firebaseUserInstance = User()
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