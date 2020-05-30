package com.th3pl4gu3.locky_offline.ui.main.main.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.repository.database.AccountRepository
import com.th3pl4gu3.locky_offline.repository.database.CardRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _signUserOut = MutableLiveData(false)

    val signUserOut: LiveData<Boolean>
        get() = _signUserOut

    val accountSize =
        Transformations.map(AccountRepository.getInstance(getApplication()).size(getUser().email)) {
            it.toString()
        }

    val cardSize =
        Transformations.map(CardRepository.getInstance(getApplication()).size(getUser().email)) {
            it.toString()
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


    internal fun getUser(): User {
        LocalStorageManager.withLogin(getApplication())
        return LocalStorageManager.get<User>(KEY_USER_ACCOUNT)!!
    }
}