package com.th3pl4gu3.locky_offline.ui.main.main.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.AccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.CardRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.DeviceRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _loadingStatus = MutableLiveData<Loading.Status>()

    val loadingStatus: LiveData<Loading.Status>
        get() = _loadingStatus

    /*
    * Accessible Functions
    */
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

    /*
    * In-accessible functions
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