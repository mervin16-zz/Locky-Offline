package com.th3pl4gu3.locky_offline.ui.main.add.device

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.BR
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Device
import com.th3pl4gu3.locky_offline.core.others.Validation
import com.th3pl4gu3.locky_offline.repository.database.repositories.DeviceRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.getString
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.resources
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.ObservableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddDeviceViewModel(application: Application) : ObservableViewModel(application) {

    /**
     * Variables
     **/
    private var _toastEvent = MutableLiveData<String>()
    private val _formValidity = MutableLiveData<String>()
    private val _hasErrors = MutableLiveData(false)
    private val _nameErrorMessage = MutableLiveData<String>()
    private val _usernameErrorMessage = MutableLiveData<String>()
    private val _passwordErrorMessage = MutableLiveData<String>()
    private var _device =
        Device()
    private var _isNewDevice = false

    /**
     * Bindable two-way binding
     **/
    var entryName: String
        @Bindable get() {
            return _device.entryName
        }
        set(value) {
            _device.entryName = value
            notifyPropertyChanged(BR.entryName)
        }

    var deviceUsername: String
        @Bindable get() {
            return _device.username
        }
        set(value) {
            _device.username = value
            notifyPropertyChanged(BR.deviceUsername)
        }

    var devicePassword: String
        @Bindable get() {
            return _device.password
        }
        set(value) {
            _device.password = value
            notifyPropertyChanged(BR.devicePassword)
        }

    var ipAddress: String?
        @Bindable get() {
            return _device.ipAddress
        }
        set(value) {
            _device.ipAddress = value
            notifyPropertyChanged(BR.ipAddress)
        }

    var macAddress: String?
        @Bindable get() {
            return _device.macAddress
        }
        set(value) {
            _device.macAddress = value
            notifyPropertyChanged(BR.macAddress)
        }

    var moreInfo: String?
        @Bindable get() {
            return _device.additionalInfo
        }
        set(value) {
            _device.additionalInfo = value
            notifyPropertyChanged(BR.moreInfo)
        }

    var accent: String
        @Bindable get() {
            return _device.accent
        }
        set(value) {
            _device.accent = value
            notifyPropertyChanged(BR.accent)
        }

    var icon: String
        @Bindable get() {
            return _device.icon
        }
        set(value) {
            _device.icon = value
            notifyPropertyChanged(BR.icon)
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

    val usernameErrorMessage: LiveData<String>
        get() = _usernameErrorMessage

    val passwordErrorMessage: LiveData<String>
        get() = _passwordErrorMessage

    val toastEvent: LiveData<String>
        get() = _toastEvent


    /**
     * Functions
     **/
    fun save() {
        viewModelScope.launch {
            _device.apply {
                val validation =
                    Validation(
                        getApplication()
                    )
                if (validation.isDeviceFormValid(this)) {

                    /* If validation succeeds, set user ID */
                    this.user = activeUser.email

                    _formValidity.value = insertDeviceInDatabase(this)

                } else {
                    _hasErrors.value = true
                    assignErrorMessages(validation.errorList)
                }
            }

        }
    }

    fun resetLogo() {
        accent =
            getString(R.string.dev_color_hex_accent_bank_logo_default)
        icon = resources.getResourceEntryName(R.drawable.ic_device)
    }

    internal fun doneWithToastEvent() {
        _toastEvent.value = null
    }

    internal fun resetErrorsFlag() {
        _hasErrors.value = false
    }

    internal fun loadDevice(currentKey: Int, previousKey: Int) {
        viewModelScope.launch {

            if (currentKey == -1 && previousKey == -1) {
                /*
                * This means it is an addition
                */
                _isNewDevice = true
                return@launch
            }

            val repository = DeviceRepository.getInstance(getApplication())

            if (currentKey == 0 && previousKey > 0) {
                /*
                * This means it is a duplicate
                */
                _device = repository.get(previousKey)!!.apply {
                    id = 0
                }

                notifyChange()

                /*
                * We need to set new account to true
                * So that it is added instead of updated
                */
                _isNewDevice = true

            } else if (currentKey > 0) {
                /* This means it is an edit */
                _device = repository.get(currentKey)!!
                notifyChange()
            }
        }
    }

    private suspend fun insertDeviceInDatabase(device: Device): String {

        var message = ""

        withContext(Dispatchers.IO) {
            if (_isNewDevice) {
                message = getString(
                    R.string.message_credentials_created,
                    device.entryName
                )
                saveCardToDatabase(device)
            } else {
                message = getString(
                    R.string.message_credentials_modified,
                    device.entryName
                )
                updateCardInDatabase(device)
            }
        }

        return message
    }

    private suspend fun updateCardInDatabase(device: Device) {
        withContext(Dispatchers.IO) {
            DeviceRepository.getInstance(getApplication()).update(device)
        }
    }

    private suspend fun saveCardToDatabase(device: Device) {
        withContext(Dispatchers.IO) {
            DeviceRepository.getInstance(getApplication()).insert(device)
        }
    }

    private fun assignErrorMessages(errorList: HashMap<Validation.ErrorField, String>) {
        _nameErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.NAME)) errorList[Validation.ErrorField.NAME] else null
        _usernameErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.USERNAME)) errorList[Validation.ErrorField.USERNAME] else null
        _passwordErrorMessage.value =
            if (errorList.containsKey(Validation.ErrorField.PASSWORD)) errorList[Validation.ErrorField.PASSWORD] else null
    }
}