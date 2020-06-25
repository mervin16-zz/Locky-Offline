package com.th3pl4gu3.locky_offline.ui.main.main.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_SECURITY_SETTINGS
import android.provider.Settings.ACTION_SETTINGS
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.openMail
import com.th3pl4gu3.locky_offline.ui.main.utils.snack
import com.th3pl4gu3.locky_offline.ui.main.utils.toast


class SettingsFragment : PreferenceFragmentCompat() {

    private var _viewModel: SettingsViewModel? = null
    private lateinit var _biometricManager: BiometricManager

    private val viewModel get() = _viewModel!!

    companion object {
        const val TAG = "SETTINGS_FRAGMENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.xml_settings_main, rootKey)
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

        /* Preference settings for backup */
        backupPreference()

        /* Preference settings for feedback */
        feedbackPreference()

        /* Preference settings for deleting data */
        wipeDataPreference()

        /* Observes the loading status */
        observeLoadingStatus()
    }

    private fun appThemePreference() {
        findPreference<ListPreference>(getString(R.string.settings_key_display_theme))?.setOnPreferenceChangeListener { preference, newValue ->
            when (newValue) {
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

            save(preference.key, newValue)

            true
        }
    }

    private fun fingerprintPreference() {
        findPreference<SwitchPreferenceCompat>(getString(R.string.settings_key_security_biometric))?.setOnPreferenceChangeListener { preference, newValue ->

            /*
            * Here we perform another check to see if biometric has bee a success
            * or if any biometric has been enrolled
            */
            when (_biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    save(preference.key, newValue)

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

    private fun emailIntent() {
        val intent = openMail(
            arrayOf(getString(R.string.app_support_email_team)),
            getString(R.string.app_support_email_subject_feedback)
        )

        if (isIntentSafeToStart(intent)) startActivity(intent) else showEmailDialog()
    }

    private fun isIntentSafeToStart(intent: Intent) =
        intent.resolveActivity(requireActivity().packageManager) != null

    private fun showEmailDialog() =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.text_title_alert_intent_none, "email"))
            .setMessage(getString(R.string.text_message_alert_intent_none, "an email"))
            .setPositiveButton(R.string.button_action_okay) { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    private fun save(key: String, value: Any) {
        LocalStorageManager.withSettings(requireActivity().application)
        LocalStorageManager.put(key, value)
    }

    private fun enrollBiometricIntent() {
        requireActivity().startActivity(Intent(ACTION_SECURITY_SETTINGS))
    }

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

    private fun biometricInitialCheck() {
        _biometricManager = BiometricManager.from(requireContext())

        when (_biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                preferenceScreen.removePreference(findPreference<ListPreference>(getString(R.string.settings_key_display_theme)))
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                findPreference<ListPreference>(getString(R.string.settings_key_display_theme))?.isEnabled =
                    false

            else -> fingerprintPreference()
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

    private fun wipeDataPreference() {
        findPreference<Preference>(getString(R.string.settings_key_data_wipe))?.setOnPreferenceClickListener {
            wipeDataConfirmationDialog()
            true
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
}
