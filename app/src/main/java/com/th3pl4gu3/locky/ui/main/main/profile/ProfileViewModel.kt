package com.th3pl4gu3.locky.ui.main.main.profile

import androidx.lifecycle.ViewModel
import com.th3pl4gu3.locky.core.User
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_USER_ACCOUNT_TYPE
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_USER_DATE_JOINED
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_USER_EMAIL
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_USER_NAME
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.PLACEHOLDER_DATA_NONE

class ProfileViewModel : ViewModel() {

    internal fun fieldList(user: User): ArrayList<UserDetails> =
        ArrayList<UserDetails>().apply {
            add(
                UserDetails(
                    LABEL_TEXTBOX_USER_NAME,
                    if (user.name.isEmpty()) PLACEHOLDER_DATA_NONE else user.name
                )
            )
            add(
                UserDetails(
                    LABEL_TEXTBOX_USER_EMAIL,
                    if (user.email.isEmpty()) PLACEHOLDER_DATA_NONE else user.email
                )
            )
            add(
                UserDetails(
                    LABEL_TEXTBOX_USER_DATE_JOINED,
                    if (user.dateJoined.isEmpty()) PLACEHOLDER_DATA_NONE else user.dateJoined
                )
            )
            add(
                UserDetails(
                    LABEL_TEXTBOX_USER_ACCOUNT_TYPE,
                    user.accountType.toString()
                )
            )
        }
}