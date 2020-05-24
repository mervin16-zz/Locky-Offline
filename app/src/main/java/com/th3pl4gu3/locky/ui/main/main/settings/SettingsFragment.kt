package com.th3pl4gu3.locky.ui.main.main.settings

import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky.ui.main.utils.toast

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.xml_settings_main, rootKey)
    }


    /*private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/

    override fun onStart() {
        super.onStart()

        appThemePreference()

        fingerprintPreference()

        backupPreference()

        restorePreference()

        feedbackPreference()
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
        findPreference<SwitchPreferenceCompat>(getString(R.string.settings_key_security_fingerprint))?.setOnPreferenceChangeListener { preference, newValue ->
            toast(
                getString(
                    R.string.dev_feature_implementation_unknown,
                    "Fingerprint Authentication"
                )
            )
            true
        }
    }

    private fun backupPreference() {
        findPreference<Preference>(getString(R.string.settings_key_backup_backup))?.setOnPreferenceClickListener {
            toast(getString(R.string.dev_feature_implementation_unknown, "Backup"))
            true
        }
    }

    private fun restorePreference() {
        findPreference<Preference>(getString(R.string.settings_key_backup_restore))?.setOnPreferenceClickListener {
            toast(getString(R.string.dev_feature_implementation_unknown, "Restore"))
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
        val intent = Intent(ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.app_support_email_team))
            ) // recipients
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_support_email_subject_feedback))
        }

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

    private fun toast(message: String) = requireContext().toast(message)

    private fun save(key: String, value: Any) {
        LocalStorageManager.with(requireActivity().application)
        LocalStorageManager.put(key, value)
    }
}
