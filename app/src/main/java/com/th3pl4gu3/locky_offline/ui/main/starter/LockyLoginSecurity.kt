package com.th3pl4gu3.locky_offline.ui.main.starter

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.SettingsManager

class LockyLoginSecurity(private val application: Application) {

    /*
    * Private Live Data
    */
    private val _launchBiometrics = MutableLiveData(false)
    private val _launchMasterPassword = MutableLiveData(false)
    private val _launchBiometricsDialog = MutableLiveData(false)
    private val _hasNoSecurityEnabled = MutableLiveData(false)

    /*
    * Accessible Live Data
    */
    val launchBiometrics: LiveData<Boolean> get() = _launchBiometrics
    val launchMasterPassword: LiveData<Boolean> get() = _launchMasterPassword
    val launchBiometricsDialog: LiveData<Boolean> get() = _launchBiometricsDialog
    val hasNoSecurityEnabled: LiveData<Boolean> get() = _hasNoSecurityEnabled

    /*
    * Accessible Functions
    */
    internal fun launchVerification() {
        when {
            isBiometricsEnabled() -> {
                biometricsPreCheck()
            }
            isMasterPasswordEnabled() -> {
                _launchMasterPassword.value = true
            }
            else -> {
                _hasNoSecurityEnabled.value = true
            }
        }
    }

    /*
    * Private functions
    */
    private fun biometricsPreCheck() {
        if (!LockyBiometrics.hasEnrollments(application)) {
            /*
             * This means that a user that previously has enrolled
             * biometrics, no longer have them
             * we hence cannot let the user proceed until
             * new enrolments are configured.
      à       * We then show a dialog to the user explaining the situation
            */
            _launchBiometricsDialog.value = true

            return
        }

        _launchBiometrics.value = true
    }

    private fun isMasterPasswordEnabled(): Boolean =
        SettingsManager(application).isMasterPasswordEnabled()

    private fun isBiometricsEnabled(): Boolean =
        SettingsManager(application).isBiometricsEnabled()
}