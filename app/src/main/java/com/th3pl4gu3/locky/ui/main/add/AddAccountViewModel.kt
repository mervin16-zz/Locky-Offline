package com.th3pl4gu3.locky.ui.main.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.core.networking.LoadingStatus
import com.th3pl4gu3.locky.core.networking.Repository
import com.th3pl4gu3.locky.core.Validation
import com.th3pl4gu3.locky.core.networking.WebsiteLogo
import com.th3pl4gu3.locky.core.exceptions.FormException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class AddAccountViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _status = MutableLiveData<LoadingStatus>()
    private val _websites = MutableLiveData<List<WebsiteLogo>>()
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

    //Other Properties
    val status: LiveData<LoadingStatus>
        get() = _status

    val websites: LiveData<List<WebsiteLogo>>
        get() = _websites

    val toastEvent: LiveData<String>
        get() = _toastEvent

    init {
        _status.value = LoadingStatus.DONE
        getWebsiteLogoProperties()
    }

    fun doneWithToastEvent(){
        _toastEvent.value = null
    }

    fun setAccount(account: Account){
        this._account = account

        _name.value = _account.name
        _username.value = _account.username
        _email.value = _account.email
        _password.value = _account.password
    }

    fun isFormValid(account: Account) {
        val validation = Validation(account)

        try {
            validation.validateAccountForm()
            _isFormValid.value = true
        } catch (ex: FormException) {
            assignErrorMessages(validation.errorList)
        } catch (ex: Exception) {
            _toastEvent.value = "Error code 2: ${ex.message}"
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

    private fun getWebsiteLogoProperties() = coroutineScope.launch {
        try {
            _status.value = LoadingStatus.LOADING
            _websites.value = Repository()
                .getWebsiteDetails()
            _status.value = LoadingStatus.DONE
        } catch (t: Throwable) {
            _status.value = LoadingStatus.ERROR
            _websites.value = ArrayList()
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }
}