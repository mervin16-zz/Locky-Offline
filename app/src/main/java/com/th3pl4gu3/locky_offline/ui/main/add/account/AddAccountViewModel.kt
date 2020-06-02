package com.th3pl4gu3.locky_offline.ui.main.add.account

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.BR
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.core.main.Validation
import com.th3pl4gu3.locky_offline.repository.database.AccountRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.ObservableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddAccountViewModel(application: Application) : ObservableViewModel(application) {

    /**
     * Variables
     **/
    private lateinit var _account: Account
    private var _toastEvent = MutableLiveData<String>()
    private val _formValidity = MutableLiveData<String>()
    private val _nameErrorMessage = MutableLiveData<String>()
    private val _passwordErrorMessage = MutableLiveData<String>()

    /**
     * Bindable two-way binding
     **/
    var accountName: String
        @Bindable get() {
            return _account.accountName
        }
        set(value) {
            _account.accountName = value
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

    var twoFa: String?
        @Bindable get() {
            return _account.authenticationType
        }
        set(value) {
            _account.authenticationType = value
            notifyPropertyChanged(BR.twoFa)
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
            return _account.accountMoreInfo
        }
        set(value) {
            _account.accountMoreInfo = value
            notifyPropertyChanged(BR.accountMoreInfo)
        }

    var logoUrl: String
        @Bindable get() {
            return _account.logoUrl
        }
        set(value) {
            _account.logoUrl = value
            notifyPropertyChanged(BR.logoUrl)
        }

    /**
     * Properties
     **/
    val formValidity: LiveData<String>
        get() = _formValidity

    val nameErrorMessage: LiveData<String>
        get() = _nameErrorMessage

    val passwordErrorMessage: LiveData<String>
        get() = _passwordErrorMessage

    val toastEvent: LiveData<String>
        get() = _toastEvent

    /**
     * Functions
     **/
    fun save() {
        viewModelScope.launch {
            _account.apply {

                val validation = Validation()
                if (validation.isAccountFormValid(this)) {
                    /* If validation succeeds, set user ID */
                    this.user = getUser().email

                    insertAccountInDatabase(this)

                    _formValidity.value = accountName
                } else {
                    assignErrorMessages(validation.errorList)
                }

            }
        }
    }

    internal fun setAccount(account: Account?) {
        this._account = account ?: Account()
    }

    internal fun doneWithToastEvent() {
        _toastEvent.value = null
    }

    private suspend fun insertAccountInDatabase(account: Account) {
        withContext(Dispatchers.IO) {
            /*
            * Firs we try to fetch account to see if it is already present
            * If already present, it means it is an edit
            * Else, it means it is an addition
            */
            val repository = AccountRepository.getInstance(getApplication())
            val fetchedAccount = repository.get(account.accountID)
            if (fetchedAccount == null) saveAccountToDatabase(account) else updateAccountInDatabase(
                account
            )
        }
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

    private fun assignErrorMessages(errorList: HashMap<Validation.ErrorField, Validation.ErrorType>) {
        _nameErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NAME)) getApplication<Application>().getString(
                R.string.error_field_validation_blank
            ) else null
        _passwordErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.PASSWORD)) getApplication<Application>().getString(
                R.string.error_field_validation_blank
            ) else null
    }

    private fun getUser(): User {
        LocalStorageManager.withLogin(getApplication())
        return LocalStorageManager.get<User>(Constants.KEY_USER_ACCOUNT)!!
    }
}