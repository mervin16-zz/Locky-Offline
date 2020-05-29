package com.th3pl4gu3.locky_offline.ui.main.add.account

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.BR
import com.th3pl4gu3.locky_offline.core.exceptions.FormException
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.Validation
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
    private val _usernameErrorMessage = MutableLiveData<String>()
    private val _emailErrorMessage = MutableLiveData<String>()
    private val _passwordErrorMessage = MutableLiveData<String>()
    private var isEmptyAccount = true

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

    var website: String?
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

    val usernameErrorMessage: LiveData<String>
        get() = _usernameErrorMessage

    val emailErrorMessage: LiveData<String>
        get() = _emailErrorMessage

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


                /*val validation = Validation(this)*/
                try {
                    /*validation.validateAccountForm()*/
                    insertAccountInDatabase(this)
                    _formValidity.value = accountName
                } catch (ex: FormException) {
                    /*assignErrorMessages(validation.errorList)*/
                } catch (ex: Exception) {
                    _toastEvent.value = "Error code 2: ${ex.message}"
                }
            }
        }
        TODO("FIX")
    }

    internal fun setAccount(account: Account?) {

        /** Check if data comes from edit screen
         *  or data is empty because it comes form add screen
         *  To do that, test if it is null or not **/
        isEmptyAccount = account == null || account.accountID.isEmpty()

        this._account = account ?: Account()
    }

    internal fun doneWithToastEvent() {
        _toastEvent.value = null
    }

    private suspend fun insertAccountInDatabase(account: Account) {
        withContext(Dispatchers.IO) {
            if (isEmptyAccount) saveAccountToDatabase(account) else updateAccountInDatabase(account)
        }
    }

    private suspend fun updateAccountInDatabase(account: Account) {
        withContext(Dispatchers.IO) {
            /* AccountRepository(getApplication()).update(account)*/
            TODO("Fix")
        }
    }

    private suspend fun saveAccountToDatabase(account: Account) {
        withContext(Dispatchers.IO) {
            /*AccountRepository(getApplication()).insert(account)*/
            TODO("Fix")
        }
    }

    private fun assignErrorMessages(errorList: HashMap<Validation.ErrorField, String>) {
        _nameErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NAME)) errorList[Validation.ErrorField.NAME] else null
        _usernameErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.USERNAME)) errorList[Validation.ErrorField.USERNAME] else null
        _emailErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.EMAIL)) errorList[Validation.ErrorField.EMAIL] else null
        _passwordErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.PASSWORD)) errorList[Validation.ErrorField.PASSWORD] else null
    }

    private fun getUserID(): String {
        return ""
        TODO("FIX")
    }
}