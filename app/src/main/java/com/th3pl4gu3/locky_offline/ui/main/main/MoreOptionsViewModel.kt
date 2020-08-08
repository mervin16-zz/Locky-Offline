package com.th3pl4gu3.locky_offline.ui.main.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.core.credentials.*
import com.th3pl4gu3.locky_offline.repository.database.repositories.AccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.CardRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.DeviceRepository
import kotlinx.coroutines.launch

class MoreOptionsViewModel(application: Application) : AndroidViewModel(application) {

    /*
    * Accessible functions
    */
    internal fun delete(credential: Credentials) {
        viewModelScope.launch {
            when (credential) {
                is Account -> AccountRepository.getInstance(getApplication()).delete(credential.id)
                is Card -> CardRepository.getInstance(getApplication()).delete(credential.id)
                is BankAccount -> BankAccountRepository.getInstance(getApplication())
                    .delete(credential.id)
                is Device -> DeviceRepository.getInstance(getApplication()).delete(credential.id)
            }
        }
    }
}