package com.th3pl4gu3.locky_offline.ui.main.add.bank_account

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.BR
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.BankAccount
import com.th3pl4gu3.locky_offline.core.others.Validation
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.getString
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.ObservableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddBankAccountViewModel(application: Application) : ObservableViewModel(application) {

    /* Private Variables */
    private var _toastEvent = MutableLiveData<String>()
    private val _formValidity = MutableLiveData<String>()
    private val _hasErrors = MutableLiveData(false)
    private val _nameErrorMessage = MutableLiveData<String>()
    private val _numberErrorMessage = MutableLiveData<String>()
    private val _bankErrorMessage = MutableLiveData<String>()
    private val _ownerErrorMessage = MutableLiveData<String>()
    private var _bankAccount =
        BankAccount()
    private var _isNewAccount = false


    /**
     * Bindable two-way binding
     **/
    var entryName: String
        @Bindable get() {
            return _bankAccount.entryName
        }
        set(value) {
            _bankAccount.entryName = value
            notifyPropertyChanged(BR.entryName)
        }

    var number: String
        @Bindable get() {
            return _bankAccount.accountNumber
        }
        set(value) {
            _bankAccount.accountNumber = value
            notifyPropertyChanged(BR.number)
        }

    var owner: String
        @Bindable get() {
            return _bankAccount.accountOwner
        }
        set(value) {
            _bankAccount.accountOwner = value
            notifyPropertyChanged(BR.owner)
        }

    var bank: String
        @Bindable get() {
            return _bankAccount.bank
        }
        set(value) {
            _bankAccount.bank = value
            notifyPropertyChanged(BR.bank)
        }

    var accent: String
        @Bindable get() {
            return _bankAccount.accent
        }
        set(value) {
            _bankAccount.accent = value
            notifyPropertyChanged(BR.accent)
        }

    var iban: String?
        @Bindable get() {
            return _bankAccount.iban
        }
        set(value) {
            _bankAccount.iban = value
            notifyPropertyChanged(BR.iban)
        }

    var swiftCode: String?
        @Bindable get() {
            return _bankAccount.swiftCode
        }
        set(value) {
            _bankAccount.swiftCode = value
            notifyPropertyChanged(BR.swiftCode)
        }

    var moreInfo: String?
        @Bindable get() {
            return _bankAccount.additionalInfo
        }
        set(value) {
            _bankAccount.additionalInfo = value
            notifyPropertyChanged(BR.moreInfo)
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

    val numberErrorMessage: LiveData<String>
        get() = _numberErrorMessage

    val bankErrorMessage: LiveData<String>
        get() = _bankErrorMessage

    val ownerErrorMessage: LiveData<String>
        get() = _ownerErrorMessage

    val toastEvent: LiveData<String>
        get() = _toastEvent

    /*
    * Accessible Functions
    */
    fun save() {
        viewModelScope.launch {
            _bankAccount.apply {
                val validation =
                    Validation(
                        getApplication()
                    )
                if (validation.isBankAccountFormValid(this)) {

                    /* If validation succeeds, set user ID */
                    this.user = activeUser.email

                    _formValidity.value = insertBankAccountInDatabase(this)

                } else {
                    _hasErrors.value = true
                    assignErrorMessages(validation.errorList)
                }
            }

        }
    }

    internal fun doneWithToastEvent() {
        _toastEvent.value = null
    }

    internal fun resetErrorsFlag() {
        _hasErrors.value = false
    }

    internal fun loadBankAccount(currentKey: Int, previousKey: Int) {
        viewModelScope.launch {
            if (currentKey == -1 && previousKey == -1) {
                /*
                * This means it is an addition
                */
                _isNewAccount = true
                return@launch
            }

            val repository = BankAccountRepository.getInstance(getApplication())

            if (currentKey == 0 && previousKey > 0) {
                /*
                * This means it is a duplicate
                */
                _bankAccount = repository.get(previousKey)!!.apply {
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
                _bankAccount = repository.get(currentKey)!!
                notifyChange()
            }
        }
    }

    /*
    * In-accessible functions
    */
    private suspend fun insertBankAccountInDatabase(account: BankAccount): String {

        var message = ""

        withContext(Dispatchers.IO) {
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

    private suspend fun updateAccountInDatabase(account: BankAccount) {
        withContext(Dispatchers.IO) {
            BankAccountRepository.getInstance(getApplication()).update(account)
        }
    }

    private suspend fun saveAccountToDatabase(account: BankAccount) {
        withContext(Dispatchers.IO) {
            BankAccountRepository.getInstance(getApplication()).insert(account)
        }
    }

    private fun assignErrorMessages(errorList: HashMap<Validation.ErrorField, String>) {
        _nameErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NAME)) errorList[Validation.ErrorField.NAME] else null
        _numberErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NUMBER)) errorList[Validation.ErrorField.NUMBER] else null
        _bankErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.BANK)) errorList[Validation.ErrorField.BANK] else null
        _ownerErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.OWNER)) errorList[Validation.ErrorField.OWNER] else null
    }
}