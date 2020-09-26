package com.th3pl4gu3.locky_offline.ui.main.add.account

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.BR
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Account
import com.th3pl4gu3.locky_offline.core.others.Validation
import com.th3pl4gu3.locky_offline.repository.database.repositories.AccountRepository
import com.th3pl4gu3.locky_offline.ui.main.main.password_generator.PasswordGenerator
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.getString
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.ObservableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddAccountViewModel(application: Application) : ObservableViewModel(application) {

    /**
     * Variables
     **/
    private var _toastEvent = MutableLiveData<String>()
    private val _formValidity = MutableLiveData<String>()
    private val _hasErrors = MutableLiveData(false)
    private val _nameErrorMessage = MutableLiveData<String>()
    private val _passwordErrorMessage = MutableLiveData<String>()
    private val _emailErrorMessage = MutableLiveData<String>()
    private var _account =
        Account()
    private var _isNewAccount = false

    /**
     * Bindable two-way binding
     **/
    var accountName: String
        @Bindable get() {
            return _account.entryName
        }
        set(value) {
            _account.entryName = value
            notifyPropertyChanged(BR.accountName)
        }

    var username: String
        @Bindable get() {
            return _account.username
        }
        set(value) {
            _account.username = value
            notifyPropertyChanged(BR.username)
        }

    var email: String
        @Bindable get() {
            return _account.email
        }
        set(value) {
            _account.email = value
            notifyPropertyChanged(BR.email)
        }

    var password: String
        @Bindable get() {
            return _account.password
        }
        set(value) {
            _account.password = value
            notifyPropertyChanged(BR.password)
        }

    var website: String
        @Bindable get() {
            return _account.website
        }
        set(value) {
            _account.website = value
            notifyPropertyChanged(BR.website)
        }

    var authType: String?
        @Bindable get() {
            return _account.authenticationType
        }
        set(value) {
            _account.authenticationType = value
            notifyPropertyChanged(BR.authType)
        }

    var twoFaKeys: String?
        @Bindable get() {
            return _account.twoFASecretKeys
        }
        set(value) {
            _account.twoFASecretKeys = value
            notifyPropertyChanged(BR.twoFaKeys)
        }

    var accountMoreInfo: String?
        @Bindable get() {
            return _account.additionalInfo
        }
        set(value) {
            _account.additionalInfo = value
            notifyPropertyChanged(BR.accountMoreInfo)
        }

    var logoUrl: String?
        @Bindable get() {
            return _account.logoUrl
        }
        set(value) {
            _account.logoUrl = value ?: ""
            notifyPropertyChanged(BR.logoUrl)
        }

    /**
     * Properties
     **/
    val formValidity: LiveData<String>
        get() = _formValidity

    val hasErrors: LiveData<Boolean>
        get() = _hasErrors

    val nameErrorMessage: LiveData<String>
        get() = _nameErrorMessage

    val passwordErrorMessage: LiveData<String>
        get() = _passwordErrorMessage

    val emailErrorMessage: LiveData<String>
        get() = _emailErrorMessage

    val toastEvent: LiveData<String>
        get() = _toastEvent

    /**
     * Functions
     **/
    fun save() {
        viewModelScope.launch {
            _account.apply {

                val validation =
                    Validation(
                        getApplication()
                    )
                if (validation.isAccountFormValid(this)) {
                    /* If validation succeeds, set user ID */
                    this.user = activeUser.email

                    _formValidity.value = insertAccountInDatabase(this)

                } else {
                    _hasErrors.value = true
                    assignErrorMessages(validation.errorList)
                }

            }
        }
    }

    fun generatePassword() = with(PasswordGenerator) {
        withOptions(
            get(getString(R.string.settings_key_passwordgen_haslower)),
            get(getString(R.string.settings_key_passwordgen_hasupper)),
            get(getString(R.string.settings_key_passwordgen_hasnumbers)),
            get(getString(R.string.settings_key_passwordgen_hasdash)),
            get(getString(R.string.settings_key_passwordgen_hasspecials))
        )

        with(generate()) {
            if (this.isEmpty()) {
                _toastEvent.value =
                    getString(R.string.message_passwordgenerator_configuration_notdone)
                return
            }
            password = this
        }
    }

    internal fun resetErrorsFlag() {
        _hasErrors.value = false
    }

    internal fun loadAccount(currentKey: Int, previousKey: Int) {
        viewModelScope.launch {
            if (currentKey == -1 && previousKey == -1) {
                /*
                * This means it is an addition
                */
                _isNewAccount = true
                return@launch
            }

            val repository = AccountRepository.getInstance(getApplication())

            if (currentKey == 0 && previousKey > 0) {
                /*
                * This means it is a duplicate
                */
                _account = repository.get(previousKey)!!.apply {
                    id = 0
                }

                notifyChange()

                /*
                * We need to set new account to true
                * So that it is added instead of updated
                */
                _isNewAccount = true

            } else if (currentKey > 0) {
                /* This means it is an edit */
                _account = repository.get(currentKey)!!
                notifyChange()
            }
        }
    }

    internal fun doneWithToastEvent() {
        _toastEvent.value = null
    }

    private suspend fun insertAccountInDatabase(account: Account): String {
        var message = ""

        withContext(Dispatchers.IO) {
            /*
            * Firs we try to fetch account to see if it is already present
            * If already present, it means it is an edit
            * Else, it means it is an addition
            */
            if (_isNewAccount) {
                message = getString(
                    R.string.message_credentials_created,
                    account.entryName
                )
                saveAccountToDatabase(account)
            } else {
                message = getString(
                    R.string.message_credentials_modified,
                    account.entryName
                )
                updateAccountInDatabase(account)
            }
        }

        return message
    }

    private suspend fun updateAccountInDatabase(account: Account) {
        withContext(Dispatchers.IO) {
            AccountRepository.getInstance(getApplication()).update(account)
        }
    }

    private suspend fun saveAccountToDatabase(account: Account) {
        withContext(Dispatchers.IO) {
            AccountRepository.getInstance(getApplication()).insert(account)
        }
    }

    private fun assignErrorMessages(errorList: HashMap<Validation.ErrorField, String>) {
        _nameErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NAME)) errorList[Validation.ErrorField.NAME] else null
        _passwordErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.PASSWORD)) errorList[Validation.ErrorField.PASSWORD] else null
        _emailErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.EMAIL)) errorList[Validation.ErrorField.EMAIL] else null
    }

    private fun get(key: String) = with(LocalStorageManager) {
        withSettings(getApplication())
        get<Boolean>(key) ?: false
    }
}