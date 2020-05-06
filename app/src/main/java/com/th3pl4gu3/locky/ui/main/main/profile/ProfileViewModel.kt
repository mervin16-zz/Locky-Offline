package com.th3pl4gu3.locky.ui.main.main.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.User

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

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
        }

    private fun getString(res: Int) = getApplication<Application>().getString(res)
}