package com.th3pl4gu3.locky.ui.main.main.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _signUserOut = MutableLiveData(false)

    val signUserOut: LiveData<Boolean>
        get() = _signUserOut

    internal fun fieldList(user: User): ArrayList<UserDetails> =
        ArrayList<UserDetails>().apply {
            add(
                UserDetails(
                    getString(R.string.field_profile_name),
                    if (user.name.isEmpty()) getString(R.string.field_placeholder_empty) else user.name
                )
            )
            add(
                UserDetails(
                    getString(R.string.field_profile_email),
                    if (user.email.isEmpty()) getString(R.string.field_placeholder_empty) else user.email
                )
            )
            add(
                UserDetails(
                    getString(R.string.field_profile_dj),
                    if (user.dateJoined.isEmpty()) getString(R.string.field_placeholder_empty) else user.dateJoined
                )
            )
            add(
                UserDetails(
                    getString(R.string.field_profile_account_type),
                    user.accountType.toString()
                )
            )
            add(
                UserDetails(
                    getString(R.string.field_profile_account_status),
                    user.accountStatus.toString()
                )
            )
        }


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