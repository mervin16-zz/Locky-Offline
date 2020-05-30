package com.th3pl4gu3.locky_offline.ui.main.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ActivityNavigator
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.databinding.ActivitySplashBinding
import com.th3pl4gu3.locky_offline.ui.main.main.MainActivity
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.openActivity
import com.th3pl4gu3.locky_offline.ui.main.utils.toast


class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private var _viewModel: SplashViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    companion object {
        const val RC_SIGN_IN = 1001
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

        /* Loads google sign in */
        googleSignInLoading()

        /* Updates the app settings*/
        updateAppSettings()

        /*Listener for Get Started Button*/
        listenerForGetStartedButton()

        /*Listener for Google Button*/
        listenerForGoogleButton()

        /* Observe sign in state */
        observeSignInState()
    }

    override fun onStart() {
        super.onStart()
        /*
        * This function checks if the user is signed in or not
        */
        checkIfUserSignedIn()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

    private fun observeSignInState() = viewModel.isSignInComplete.observe(this, Observer {
        if (it) {
            navigateToMain()
        }
    })

    private fun listenerForGetStartedButton() = binding.ButtonGetStarted.setOnClickListener {
        viewModel.showGoogleButton()
    }

    private fun listenerForGoogleButton() = binding.ButtonGoogle.setOnClickListener {
        viewModel.showLoading()
        launchOAuth()
    }

    private fun launchOAuth() = startActivityForResult(mGoogleSignInClient.signInIntent, RC_SIGN_IN)

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)!!

            viewModel.login(User.getInstance(account))

        } catch (e: ApiException) {
            when (e.statusCode) {
                GoogleSignInStatusCodes.NETWORK_ERROR -> toast(getString(R.string.message_internet_connection_unavailable))
                GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> toast(getString(R.string.message_user_signin_cancelled))
                GoogleSignInStatusCodes.INTERNAL_ERROR -> toast(e.message.toString())
                else -> toast(getString(R.string.error_internal_code_1, e.message.toString()))
            }
        }

        viewModel.showGoogleButton()
    }

    private fun checkIfUserSignedIn() {
        /*
        * We get the account from Google.
        * If it returns null, it means the user was not signed in
        * Else, the user has already been signed in and we can redirect the user to main
        */
        val account = GoogleSignIn.getLastSignedInAccount(this)

        when {
            account != null && viewModel.isUserSavedInSession() -> {
                navigateToMain()
            }
            account != null -> {
                viewModel.login(User.getInstance(account))
            }
            else -> {
                viewModel.showGetStartedButton()
            }
        }
    }

    private fun googleSignInLoading() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
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