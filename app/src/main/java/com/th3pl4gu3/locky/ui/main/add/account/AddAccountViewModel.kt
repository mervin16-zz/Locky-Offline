package com.th3pl4gu3.locky.ui.main.add.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.core.Validation
import com.th3pl4gu3.locky.core.exceptions.FormException
import com.th3pl4gu3.locky.repository.database.AccountDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddAccountViewModel : ViewModel() {


    private lateinit var _account: Account
    private var _toastEvent = MutableLiveData<String>()

    //Form Validation variables
    private val _isFormValid = MutableLiveData(false)
    private val _name = MutableLiveData<String>()
    private val _nameErrorMessage = MutableLiveData<String>()
    private val _username = MutableLiveData<String>()
    private val _usernameErrorMessage = MutableLiveData<String>()
    private val _email = MutableLiveData<String>()
    private val _emailErrorMessage = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    private val _passwordErrorMessage = MutableLiveData<String>()
    private val _logoUrl = MutableLiveData<String>()
    private val _website = MutableLiveData<String>()
    private val _2faEnabled = MutableLiveData<String>()
    private val _2faKeys = MutableLiveData<String>()
    private val _additionalInfo = MutableLiveData<String>()

    private var isEmptyAccount = true

    //Validation Properties
    val isFormValid: LiveData<Boolean>
        get() = _isFormValid

    val name: LiveData<String>
        get() = _name

    val nameErrorMessage: LiveData<String>
        get() = _nameErrorMessage

    val username: LiveData<String>
        get() = _username

    val usernameErrorMessage: LiveData<String>
        get() = _usernameErrorMessage

    val email: LiveData<String>
        get() = _email

    val emailErrorMessage: LiveData<String>
        get() = _emailErrorMessage

    val password: LiveData<String>
        get() = _password

    val passwordErrorMessage: LiveData<String>
        get() = _passwordErrorMessage

    val logoUrl: LiveData<String>
        get() = _logoUrl

    val website: LiveData<String>
        get() = _website

    val twoFaEnabled: LiveData<String>
        get() = _2faEnabled

    val twoFaKeys: LiveData<String>
        get() = _2faKeys

    val additionalInfo: LiveData<String>
        get() = _additionalInfo


    //Other Properties
    val toastEvent: LiveData<String>
        get() = _toastEvent


    fun doneWithToastEvent(){
        _toastEvent.value = null
    }

    fun setAccount(account: Account){

        /** Check if data comes from edit screen
         *  or data is empty because it comes form add screen
         *  To do that, test if the id is empty or not **/
        isEmptyAccount = account.id.isEmpty()

        this._account = account.also {
            _name.value = it.name
            _username.value = it.username
            _email.value = it.email
            _password.value = it.password
            _logoUrl.value = it.logoUrl
            _website.value = it.website
            _2faEnabled.value = it.twoFA
            _2faKeys.value = it.twoFASecretKeys
            _additionalInfo.value = it.additionalInfo
        }
    }

    fun isFormValid(account: Account) {
        viewModelScope.launch {
            val validation = Validation(account)
            try {
                validation.validateAccountForm()
                insertAccountInDatabase(account)
                _isFormValid.value = true
            } catch (ex: FormException) {
                assignErrorMessages(validation.errorList)
            } catch (ex: Exception) {
                _toastEvent.value = "Error code 2: ${ex.message}"
            }
        }
    }

    fun setAccountLogo(logoUrl: String) {
        _logoUrl.value = logoUrl
    }


    private suspend fun insertAccountInDatabase(account: Account) {
        withContext(Dispatchers.IO) {
            if (isEmptyAccount) saveAccountToDatabase(account) else updateAccountInDatabase(account)
        }
    }

    private suspend fun updateAccountInDatabase(account: Account) {
        withContext(Dispatchers.IO) {
            AccountDao().update(account)
        }
    }

    private suspend fun saveAccountToDatabase(account: Account) {
        withContext(Dispatchers.IO) {
            AccountDao().save(account)
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
}