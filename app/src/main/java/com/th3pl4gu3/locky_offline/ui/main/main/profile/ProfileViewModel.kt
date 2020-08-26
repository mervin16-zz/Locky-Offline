package com.th3pl4gu3.locky_offline.ui.main.main.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.others.Statistic
import com.th3pl4gu3.locky_offline.repository.database.repositories.AccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.CardRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.DeviceRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_USER_ACCOUNT

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _signUserOut = MutableLiveData(false)

    val signUserOut: LiveData<Boolean>
        get() = _signUserOut

    val accountSize =
        Transformations.map(
            AccountRepository.getInstance(getApplication()).size(activeUser.email)
        ) {
            Statistic(
                it,
                application.getString(R.string.locky_section_main_account)
            )
        }

    val cardSize =
        Transformations.map(CardRepository.getInstance(getApplication()).size(activeUser.email)) {
            Statistic(
                it,
                application.getString(R.string.locky_section_main_card)
            )
        }

    val bankAccountSize =
        Transformations.map(
            BankAccountRepository.getInstance(getApplication()).size(activeUser.email)
        ) {
            Statistic(
                it,
                application.getString(R.string.locky_section_main_bank_account)
            )
        }

    val deviceSize =
        Transformations.map(
            DeviceRepository.getInstance(getApplication()).size(activeUser.email)
        ) {
            Statistic(
                it,
                application.getString(R.string.locky_section_main_device)
            )
        }

    fun removeUserSession() {
        /*
        * Removes the user session
        */
        LocalStorageManager.withLogin(getApplication())
        LocalStorageManager.remove(KEY_USER_ACCOUNT)

        /* Set value to true to sign user out from firebase*/
        _signUserOut.value = true
    }
}