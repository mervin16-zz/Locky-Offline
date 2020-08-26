package com.th3pl4gu3.locky_offline.ui.main.utils.helpers

import android.app.Application
import com.th3pl4gu3.locky_offline.R


class SettingsManager(val application: Application) {

    /* An instance to LocalStorageManager */
    private val _sharedPreferences = with(LocalStorageManager) {
        withSettings(application)
        this
    }

    /* Verifications */
    fun isCardExpirationEnabled(): Boolean = with(_sharedPreferences) {
        exists(application.getString(R.string.settings_key_features_card_expiration)) &&
                get<Boolean>(application.getString(R.string.settings_key_features_card_expiration))!!
    }

    fun isMasterPasswordEnabled(): Boolean = with(_sharedPreferences) {
        exists(application.getString(R.string.settings_key_security_thepassword))
    }

    fun isBiometricsEnabled(): Boolean = with(_sharedPreferences) {
        exists(application.getString(R.string.settings_key_security_biometric)) &&
                get<Boolean>(application.getString(R.string.settings_key_security_biometric))!!
    }

    /* Fetching */
    fun getMasterPassword() = with(_sharedPreferences) {
        get<String>(application.getString(R.string.settings_key_security_thepassword))!!
    }
}