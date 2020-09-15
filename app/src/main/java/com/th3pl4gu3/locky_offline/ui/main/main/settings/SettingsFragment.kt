package com.th3pl4gu3.locky_offline.ui.main.main.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_SECURITY_SETTINGS
import android.provider.Settings.ACTION_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialSharedAxis
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.CustomViewDialogMasterpasswordBinding
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.ui.main.starter.LockyBiometrics
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.snack
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.LockyUtil.openMail

class SettingsFragment : PreferenceFragmentCompat() {

    private var _viewModel: SettingsViewModel? = null
    private var _dialogBinding: CustomViewDialogMasterpasswordBinding? = null

    private val viewModel get() = _viewModel!!
    private val dialogBinding get() = _dialogBinding!!

    companion object {
        const val TAG = "SETTINGS_FRAGMENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Page Enter Transition*/
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        /* Initiate View Model*/
        _viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        /*
        * We update the live data whether
        * ability to change master password should be enabled
        * or disabled.
        */
        viewModel.toggleMasterPasswordChange(isMasterPasswordEnabled())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        /*
        * The actual xml file that represents the
        * settings screen
        */
        setPreferencesFromResource(R.xml.xml_settings_main, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        with(super.onCreateView(inflater, container, savedInstanceState)) {
            // Set the background color
            this?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorOnSurface
                )
            )
            return this
        }
    }

    override fun onStart() {
        super.onStart()
        /*
        * Here, we initialize biometric manager
        * Then we check if biometric is available or hardware is available to determine
        * whether to show the biometric option in settings
        */
        biometricInitialCheck()

        /* Preference config for app theme */
        appThemePreference()

        /* Preference settings for Card expiration */
        cardExpirationPreference()

        /* Preference settings for backup */
        backupPreference()

        /* Preference settings for feedback */
        feedbackPreference()

        /* Preference settings for deleting data */
        wipeDataPreference()

        /* Master password preference */
        masterPasswordPreference()

        /* Change master password preference */
        changeMasterPasswordPreference()

        /* Observes the loading status */
        observeLoadingStatus()

        /* Observe if options to change master password is available */
        observeMasterPasswordChange()

        /* Observe if master password change completed */
        observeMasterPasswordValidity()

        /* Observe error messages for Master Password */
        observeMasterPasswordErrorMessageEvents()
    }


    /****************************************************\
    \******************** Preferences *******************\
    \****************************************************/
    private fun wipeDataPreference() {
        findPreference<Preference>(getString(R.string.settings_key_data_wipe))?.setOnPreferenceClickListener {
            wipeDataConfirmationDialog()
            true
        }
    }

    private fun backupPreference() {
        findPreference<Preference>(getString(R.string.settings_key_backup_backup))?.setOnPreferenceClickListener {
            backupDialog()
            true
        }
    }

    private fun feedbackPreference() {
        findPreference<Preference>(getString(R.string.settings_key_help_feedback))?.setOnPreferenceClickListener {

            emailIntent()

            true
        }
    }

    private fun appThemePreference() {
        findPreference<ListPreference>(getString(R.string.settings_key_display_theme))?.setOnPreferenceChangeListener { preference, newValue ->
            viewModel.updateAppTheme(preference.key, newValue as String)
            true
        }
    }

    private fun cardExpirationPreference() {
        findPreference<SwitchPreferenceCompat>(getString(R.string.settings_key_features_card_expiration))?.setOnPreferenceChangeListener { preference, newValue ->

            viewModel.save(preference.key, newValue)

            true
        }
    }

    private fun fingerprintPreference() {
        findPreference<SwitchPreferenceCompat>(getString(R.string.settings_key_security_biometric))?.setOnPreferenceChangeListener { preference, newValue ->
            /*
            * Here we perform another check to see if biometric has bee a success
            * or if any biometric has been enrolled
            */
            when (LockyBiometrics.getValidAuthenticators(requireActivity().application)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    viewModel.save(preference.key, newValue)

                    if (newValue as Boolean) toast(getString(R.string.message_settings_biometric_enabled))

                    true
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    biometricEnrollmentConfirmation()
                    false
                }

                else -> false
            }
        }
    }

    private fun masterPasswordPreference() {
        findPreference<SwitchPreferenceCompat>(getString(R.string.settings_key_security_password))?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                /*
                * Master password as been activated
                * We need to enable the option to change
                * the master password.
                * Then we show a dialog for user
                * to create a new master password
                */
                setMasterPasswordDialog()

                false
            } else {
                /*
                * Master password has been deactivated.
                * We need to disable the option to change
                * master password and clear the password
                * previously set
                */
                viewModel.remove(getString(R.string.settings_key_security_thepassword))
                viewModel.toggleMasterPasswordChange(false)
                true
            }
        }
    }

    private fun changeMasterPasswordPreference() {
        findPreference<Preference>(getString(R.string.settings_key_security_password_change))?.setOnPreferenceClickListener {
            changeMasterPasswordDialog()
            true
        }
    }

    /***********************************************\
    \************ LiveData Observations ************\
    \***********************************************/
    private fun observeMasterPasswordErrorMessageEvents() {
        with(viewModel) {
            currentPasswordErrorMessage.observe(viewLifecycleOwner, {
                dialogBinding.MasterPasswordCurrent.error = it
            })

            newPasswordErrorMessage.observe(viewLifecycleOwner, {
                dialogBinding.MasterPasswordNew.error = it
            })

            confirmPasswordErrorMessage.observe(viewLifecycleOwner, {
                dialogBinding.MasterPasswordConfirm.error = it
            })
        }
    }

    private fun observeLoadingStatus() {
        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it) {
                    Loading.Status.LOADING -> requireView().snack(getString(R.string.message_settings_wipe_started)) {}
                    Loading.Status.DONE -> requireView().snack(getString(R.string.message_settings_wipe_completed)) {}
                    Loading.Status.ERROR -> requireView().snack(getString(R.string.message_settings_wipe_error)) {}
                    else -> return@Observer
                }
            }
        })
    }

    private fun observeMasterPasswordChange() {
        viewModel.canChangeMasterPassword.observe(viewLifecycleOwner, {
            findPreference<Preference>(getString(R.string.settings_key_security_password_change))?.isEnabled =
                it
        })
    }

    private fun observeMasterPasswordValidity() {
        viewModel.isMasterPasswordValid.observe(viewLifecycleOwner, {
            if (it) {
                findPreference<SwitchPreferenceCompat>(getString(R.string.settings_key_security_password))?.isChecked =
                    true
                /* Updates live data */
                viewModel.toggleMasterPasswordChange(true)
                viewModel.resetMasterPasswordChangeValidity()
            }
        })
    }

    /***********************************************\
    \******************** Dialogs *******************\
    \************************************************/
    private fun wipeDataConfirmationDialog() =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.text_title_alert_data_wipe))
            .setMessage(getString(R.string.text_message_alert_data_wipe))
            .setNegativeButton(R.string.button_action_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.button_action_delete) { _, _ ->
                viewModel.deleteData()
            }
            .show()

    private fun setMasterPasswordDialog() {
        _dialogBinding =
            CustomViewDialogMasterpasswordBinding.inflate(layoutInflater, null, false)
        dialogBinding.viewModel = viewModel
        dialogBinding.isChangePassword = false

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.text_title_settings_master_password))
            .setView(dialogBinding.root)
            .setNegativeButton(R.string.button_action_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.button_action_confirm, null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            with(dialogBinding) {
                val valid = this@SettingsFragment.viewModel.createMasterPassword(
                    MasterPasswordNew.editText?.text.toString(),
                    MasterPasswordConfirm.editText?.text.toString()
                )

                if (valid) {
                    toast(getString(R.string.message_settings_masterpassword_enable))
                    dialog.dismiss()
                }
            }
        }
    }

    private fun changeMasterPasswordDialog() {
        _dialogBinding =
            CustomViewDialogMasterpasswordBinding.inflate(layoutInflater, null, false)
        dialogBinding.viewModel = viewModel
        dialogBinding.isChangePassword = true

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.text_title_settings_master_password))
            .setView(dialogBinding.root)
            .setNegativeButton(R.string.button_action_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.button_action_confirm, null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            with(dialogBinding) {
                val valid = this@SettingsFragment.viewModel.changeMasterPassword(
                    MasterPasswordCurrent.editText?.text.toString(),
                    MasterPasswordNew.editText?.text.toString(),
                    MasterPasswordConfirm.editText?.text.toString()
                )

                if (valid) {
                    toast(getString(R.string.message_settings_masterpassword_changed))
                    dialog.dismiss()
                }
            }
        }
    }

    private fun backupDialog() =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.text_title_alert_backup))
            .setMessage(getString(R.string.text_title_alert_backup_message))
            .setNegativeButton(R.string.button_action_backup_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.button_action_backup_proceed) { _, _ ->
                startActivity(Intent(ACTION_SETTINGS))
            }
            .show()

    private fun biometricEnrollmentConfirmation() =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.text_title_alert_biometric))
            .setMessage(getString(R.string.text_title_alert_biometric_message))
            .setNegativeButton(R.string.button_action_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.button_action_okay) { _, _ ->
                enrollBiometricIntent()
            }
            .show()

    private fun showEmailDialog() =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                getString(
                    R.string.text_title_alert_intent_none,
                    getString(R.string.word_email)
                )
            )
            .setMessage(
                getString(
                    R.string.text_message_alert_intent_none,
                    getString(R.string.word_email_preposition)
                )
            )
            .setPositiveButton(R.string.button_action_okay) { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    /*********************************\
    \************ Intents ************\
    \*********************************/
    private fun emailIntent() {
        val intent = openMail(
            arrayOf(getString(R.string.app_support_email_team)),
            getString(R.string.app_support_email_subject_feedback)
        )

        if (isIntentSafeToStart(intent)) startActivity(intent) else showEmailDialog()
    }

    private fun isIntentSafeToStart(intent: Intent) =
        intent.resolveActivity(requireActivity().packageManager) != null

    private fun enrollBiometricIntent() {
        requireActivity().startActivity(Intent(ACTION_SECURITY_SETTINGS))
    }


    /************************************\
    \************ Operations ************\
    \************************************/
    private fun biometricInitialCheck() {
        when (LockyBiometrics.getValidAuthenticators(requireActivity().application)) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                preferenceScreen.removePreference(findPreference<ListPreference>(getString(R.string.settings_key_display_theme)))
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                findPreference<ListPreference>(getString(R.string.settings_key_display_theme))?.isEnabled =
                    false

            else -> fingerprintPreference()
        }
    }

    private fun isMasterPasswordEnabled() =
        findPreference<SwitchPreferenceCompat>(getString(R.string.settings_key_security_password))?.isChecked!!
}
