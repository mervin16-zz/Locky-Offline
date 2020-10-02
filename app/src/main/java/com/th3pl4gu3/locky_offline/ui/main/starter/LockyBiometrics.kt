package com.th3pl4gu3.locky_offline.ui.main.starter

import android.app.Application
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.th3pl4gu3.locky_offline.R

class LockyBiometrics(private val application: Application) :
    BiometricPrompt.AuthenticationCallback() {

    internal val isSuccess = MutableLiveData(false)
    internal val errMessage = MutableLiveData<String>()

    companion object {
        /*
         * Different Flavours of Enrollments
        */
        internal fun hasEnrollments(application: Application): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                hasAnyEnrollments(application)
            } else {
                hasEnrolledBiometrics(application)
            }
        }

        internal fun getValidAuthenticators(application: Application): Int {
            return BiometricManager.from(application.applicationContext)
                .canAuthenticate(
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                        BiometricManager.Authenticators.BIOMETRIC_STRONG
                    } else {
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    }
                )
        }

        private fun hasEnrolledBiometrics(application: Application) =
            BiometricManager.from(application.applicationContext).canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            ) != BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED

        private fun hasAnyEnrollments(application: Application) =
            BiometricManager.from(application.applicationContext).canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            ) != BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        isSuccess.value = true
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        when (errorCode) {
            BiometricPrompt.ERROR_LOCKOUT -> errMessage.value =
                application.getString(R.string.error_biometric_authentication_lockout)
            BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> errMessage.value =
                application.getString(R.string.error_biometric_authentication_lockout_permanent)
            BiometricPrompt.ERROR_USER_CANCELED -> errMessage.value =
                application.getString(R.string.error_biometric_authentication_cancelled)
            BiometricPrompt.ERROR_NEGATIVE_BUTTON -> errMessage.value =
                application.getString(R.string.message_biometrics_use_masterpassword)
            else -> errMessage.value = application.getString(R.string.error_internal_code_4)
        }
    }

    fun prompt(fragment: Fragment): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(application.applicationContext)
        return BiometricPrompt(
            fragment,
            executor,
            this@LockyBiometrics
        )
    }

    fun authenticate(prompt: BiometricPrompt, isMasterPasswordEnabled: Boolean) {
        /* Determine prompt info type */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            prompt.authenticate(getPromptInfoWithIntegratedSwitching(isMasterPasswordEnabled))
        } else {
            prompt.authenticate(
                getPromptInfo(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            )
        }
    }

    /*
    * Prompt Info
    */
    private fun getPromptInfoWithIntegratedSwitching(isMasterPasswordEnabled: Boolean) =
        BiometricPrompt.PromptInfo
            .Builder()
            .setTitle(application.getString(R.string.text_title_alert_biometric_authentication))
            .setSubtitle(application.getString(R.string.text_title_alert_biometric_authentication_message))
            .setNegativeButtonText(
                /*
                * We determine which text to show as the negative button
                * If master password was not enabled, negative text will be cancel
                * else negative text will be to use master password instead
                */
                if (isMasterPasswordEnabled) application.getString(
                    R.string.button_action_use_masterpassword
                ) else application.getString(R.string.button_action_cancel)
            )
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

    private fun getPromptInfo(authenticators: Int) = BiometricPrompt.PromptInfo
        .Builder()
        .setTitle(application.getString(R.string.text_title_alert_biometric_authentication))
        .setSubtitle(application.getString(R.string.text_title_alert_biometric_authentication_message))
        .setAllowedAuthenticators(authenticators)
        .build()
}