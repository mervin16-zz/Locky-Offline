package com.th3pl4gu3.locky_offline.ui.main.main.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _signUserOut = MutableLiveData(false)

    val signUserOut: LiveData<Boolean>
        get() = _signUserOut

    fun removeUserSession() {
        /*
        * Removes the user session
        */
        LocalStorageManager.with(getApplication())
        LocalStorageManager.remove(KEY_USER_ACCOUNT)

        /* Set value to true to sign user out from firebase*/
        _signUserOut.value = true
    }

    private fun getString(res: Int) = getApplication<Application>().getString(res)
}