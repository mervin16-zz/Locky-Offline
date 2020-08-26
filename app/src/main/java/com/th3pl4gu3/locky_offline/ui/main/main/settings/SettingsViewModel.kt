package com.th3pl4gu3.locky_offline.ui.main.main.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.others.Validation
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.AccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.CardRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.DeviceRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.hash
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.updateAppTheme
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.SettingsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    /*
    * Private variables
    */
    private val _loadingStatus = MutableLiveData<Loading.Status>()
    private val _changeMasterPasswordAvailability = MutableLiveData(false)
    private val _masterPasswordValid = MutableLiveData(false)
    private val _currentPasswordErrorMessage = MutableLiveData<String>()
    private val _newPasswordErrorMessage = MutableLiveData<String>()
    private val _confirmPasswordErrorMessage = MutableLiveData<String>()

    /*
    * Public Properties
    */
    val loadingStatus: LiveData<Loading.Status>
        get() = _loadingStatus

    val canChangeMasterPassword: LiveData<Boolean>
        get() = _changeMasterPasswordAvailability

    val isMasterPasswordValid: LiveData<Boolean>
        get() = _masterPasswordValid

    val currentPasswordErrorMessage: LiveData<String>
        get() = _currentPasswordErrorMessage

    val newPasswordErrorMessage: LiveData<String>
        get() = _newPasswordErrorMessage

    val confirmPasswordErrorMessage: LiveData<String>
        get() = _confirmPasswordErrorMessage


    /*
    * Accessible Functions
    */
    internal fun toggleMasterPasswordChange(newValue: Boolean) {
        _changeMasterPasswordAvailability.value = newValue
    }

    internal fun resetMasterPasswordChangeValidity() {
        _masterPasswordValid.value = false
    }

    internal fun deleteData() {
        viewModelScope.launch {
            try {
                _loadingStatus.value = Loading.Status.LOADING
                wipeAccounts()
                wipeCards()
                wipeBankAccounts()
                wipeDevices()
                delay(3000)
                _loadingStatus.value = Loading.Status.DONE
            } catch (e: Exception) {
                _loadingStatus.value = Loading.Status.ERROR
            }
        }
    }

    internal fun createMasterPassword(newPass: String, confirmPass: String): Boolean {
        with(Validation(getApplication())) {
            if (isMaterPasswordValid(newPass, confirmPass)) {
                _masterPasswordValid.value = true
                /*
                * We first hash the password
                * then we save the password
                */
                save(
                    application.getString(R.string.settings_key_security_thepassword),
                    newPass.hash
                )
                return true
            }

            //Update error messages
            _newPasswordErrorMessage.value =
                if (errorList.containsKey(Validation.ErrorField.PASSWORD)) errorList[Validation.ErrorField.PASSWORD] else null

            _confirmPasswordErrorMessage.value =
                if (errorList.containsKey(Validation.ErrorField.CONFIRM_PASSWORD)) errorList[Validation.ErrorField.CONFIRM_PASSWORD] else null

            return false
        }
    }

    internal fun changeMasterPassword(
        enteredCurrent: String,
        newPass: String,
        confirmPass: String
    ): Boolean {
        with(Validation(getApplication())) {
            val savedMasterPassword = SettingsManager(getApplication()).getMasterPassword()

            if (isNewMaterPasswordValid(
                    savedMasterPassword,
                    enteredCurrent.hash,
                    newPass,
                    confirmPass
                )
            ) {
                _masterPasswordValid.value = true
                /*
                * We first hash the password
                * then we save the password
                */
                save(
                    application.getString(R.string.settings_key_security_thepassword),
                    newPass.hash
                )
                return true
            }

            //Update error messages
            _currentPasswordErrorMessage.value =
                if (errorList.containsKey(Validation.ErrorField.CURRENT_PASSWORD)) errorList[Validation.ErrorField.CURRENT_PASSWORD] else null

            _newPasswordErrorMessage.value =
                if (errorList.containsKey(Validation.ErrorField.PASSWORD)) errorList[Validation.ErrorField.PASSWORD] else null

            _confirmPasswordErrorMessage.value =
                if (errorList.containsKey(Validation.ErrorField.CONFIRM_PASSWORD)) errorList[Validation.ErrorField.CONFIRM_PASSWORD] else null

            return false
        }
    }

    internal fun updateAppTheme(key: String, value: String) {
        /* Save the new selected theme */
        save(key, value)

        /* Sets the theme */
        getApplication<Application>().updateAppTheme()
    }

    internal fun save(key: String, value: Any) = with(LocalStorageManager) {
        withSettings(getApplication())
        put(key, value)
    }


    internal fun remove(key: String) = with(LocalStorageManager) {
        withSettings(getApplication())
        remove(key)
    }

    /*
    * Inaccessible functions
    */
    private suspend fun wipeAccounts() =
        AccountRepository.getInstance(getApplication()).wipe(activeUser.email)

    private suspend fun wipeCards() =
        CardRepository.getInstance(getApplication()).wipe(activeUser.email)

    private suspend fun wipeBankAccounts() =
        BankAccountRepository.getInstance(getApplication()).wipe(activeUser.email)

    private suspend fun wipeDevices() =
        DeviceRepository.getInstance(getApplication()).wipe(activeUser.email)
}