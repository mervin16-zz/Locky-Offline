package com.th3pl4gu3.locky_offline.ui.main.starter

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentStarterBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.requireMainActivity
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast

class StarterFragment : Fragment() {

    private var _binding: FragmentStarterBinding? = null
    private var _viewModel: StarterViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    private lateinit var _lockySignIn: LockySignIn

    private val mainToolBar: MaterialToolbar
        get() = requireMainActivity().findViewById(R.id.Toolbar_Main)

    companion object {
        private const val RC_SIGN_IN = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreate(savedInstanceState)
        /*
         * Bind splash screen and instantiate view model
         * Bind view model to layout and set lifecycle owner
         */
        _binding = FragmentStarterBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(StarterViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        /* Loads google sign in */
        _lockySignIn = LockySignIn(requireActivity().application)
        _lockySignIn.buildGoogleSignInClient(requireActivity())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Hide Main Toolbar on startup */
        toggleMainToolbarVisibility(View.GONE)

        /*Listener for Get Started Button*/
        listenerForGetStartedButton()

        /*Listener for Google Button*/
        listenerForGoogleButton()

        /* Observe sign in state */
        observeSignInState()

        /* Observe if can navigate to main screen */
        observeIfCanNavigateToMainScreen()
    }

    override fun onStart() {
        super.onStart()

        /*
        * This function checks if the user is signed in or not
        */
        with(_lockySignIn) {
            showGetStartedButton.observe(viewLifecycleOwner, {
                if (it) {
                    viewModel.showGetStartedButton()
                }
            })

            showGoogleButton.observe(viewLifecycleOwner, {
                if (it) {
                    viewModel.showGoogleButton()
                }
            })

            proceedToLogin.observe(viewLifecycleOwner, {
                if (it != null) {
                    viewModel.login(it)
                }
            })

            navigateToMainScreen.observe(viewLifecycleOwner, {
                if (it) {
                    prepareToNavigateToMainScreen()
                }
            })

            message.observe(viewLifecycleOwner, {
                if (it != null) {
                    toast(it)
                }
            })

            isUserSignedIn()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        /* Show Main Toolbar on view destroy */
        toggleMainToolbarVisibility(View.VISIBLE)

        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            _lockySignIn.handleSignInResult(data)
        }
    }

    private fun observeIfCanNavigateToMainScreen() {
        viewModel.canNavigateToMainScreen.observe(viewLifecycleOwner, {
            if (it) {
                navigateToMainScreen()
            }
        })
    }

    private fun observeSignInState() =
        viewModel.isSignInComplete.observe(viewLifecycleOwner, {
            if (it) {
                prepareToNavigateToMainScreen()
            }
        })

    private fun listenerForGetStartedButton() = binding.ButtonGetStarted.setOnClickListener {
        viewModel.showGoogleButton()
    }

    private fun listenerForGoogleButton() = binding.ButtonGoogle.setOnClickListener {
        viewModel.showLoading()
        launchOAuth()
    }

    private fun launchOAuth() =
        startActivityForResult(_lockySignIn.googleSignInClient.signInIntent, RC_SIGN_IN)

    private fun prepareToNavigateToMainScreen() {
        /*
        * Before navigation, we need to check if
        * 1. Master Password has been enabled on the app
        * 2. Or biometrics prompt has been enabled on the app
        * If master password has been enabled, we prompt the user for a password
        * If biometrics has been enabled, we need prompt the user for his biometrics
        * If none has been enabled, we then navigate to main screen directly
        * NOTE: If both has been enabled, we prioritize biometrics over password.
        */

        with(LockyLoginSecurity(requireActivity().application), {

            launchBiometrics.observe(viewLifecycleOwner, {
                if (it) {
                    promptBiometric()
                }
            })

            launchMasterPassword.observe(viewLifecycleOwner, {
                if (it) {
                    masterPasswordVerification()
                }
            })

            launchBiometricsDialog.observe(viewLifecycleOwner, {
                if (it) {
                    biometricEnrolmentDialog()
                }
            })

            hasNoSecurityEnabled.observe(viewLifecycleOwner, {
                if (it) {
                    viewModel.canNavigateToMainScreen.value = true
                }
            })

            launchVerification()
        })
    }

    private fun masterPasswordVerification() {
        if (findNavController().currentDestination?.id == R.id.Fragment_Starter) {
            navigateTo(
                StarterFragmentDirections.actionFragmentStarterToFragmentBottomDialogPasswordAuth(
                    viewModel.fetchMasterPassword()
                )
            )
        }
    }

    private fun promptBiometric() {
        val biometrics = LockyBiometrics(requireActivity().application)
        val prompt = biometrics.prompt(this)
        biometrics.errMessage.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it) {
                    getString(R.string.message_biometrics_use_masterpassword) -> {
                        /*
                        * If the user clicks on the negative button
                        * we need to check if master password has been enabled
                        * if yes, we need to prompt the user with master password
                        */
                        if (viewModel.isMasterPasswordEnabled()) {
                            toast(it)

                            /* Start master password dialog */
                            masterPasswordVerification()

                            return@observe
                        }

                        /* If code reached this, it means master password was not enabled */
                        toast(getString(R.string.error_biometric_authentication_cancelled))
                    }
                    else -> toast(it)
                }

                requireActivity().finish()
            }
        })

        biometrics.isSuccess.observe(viewLifecycleOwner, {
            if (it) {
                viewModel.canNavigateToMainScreen.value = it
            }
        })

        biometrics.authenticate(prompt, viewModel.isMasterPasswordEnabled())
    }

    private fun navigateToMainScreen() {
        navigateTo(StarterFragmentDirections.actionFragmentStarterToFragmentAccount())
    }

    private fun biometricEnrolmentDialog() =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.text_title_alert_biometric))
            .setMessage(getString(R.string.text_title_alert_biometric_enrolments_message))
            .setNegativeButton(R.string.button_action_cancel) { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
            .setPositiveButton(R.string.button_action_okay) { dialog, _ ->
                startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
                dialog.dismiss()
            }
            .show()

    private fun toggleMainToolbarVisibility(visibility: Int) {
        mainToolBar.visibility = visibility
    }
}
