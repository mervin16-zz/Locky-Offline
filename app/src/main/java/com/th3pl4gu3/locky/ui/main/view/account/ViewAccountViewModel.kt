package com.th3pl4gu3.locky.ui.main.view.account

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.repository.database.AccountDao
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_ACCOUNT_2FA
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_ACCOUNT_2FAKEYS
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_ACCOUNT_ADDITIONAL_COMMENTS
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_ACCOUNT_EMAIL
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_ACCOUNT_PASSWORD
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_ACCOUNT_USERNAME
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_ACCOUNT_WEBSITE
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.PLACEHOLDER_DATA_NONE
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.PLACEHOLDER_DATA_PASSWORD_HIDDEN
import com.th3pl4gu3.locky.ui.main.view.CredentialsField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewAccountViewModel : ViewModel() {

    internal fun delete(key: String) {
        viewModelScope.launch {
            deleteData(key)
        }
    }

    private suspend fun deleteData(key: String) {
        withContext(Dispatchers.IO) {
            AccountDao().remove(key)
        }
    }

    internal fun fieldList(account: Account): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(
                CredentialsField(
                    LABEL_TEXTBOX_ACCOUNT_USERNAME,
                    if (account.username.isEmpty()) PLACEHOLDER_DATA_NONE else account.username,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_ACCOUNT_EMAIL,
                    if (account.email.isEmpty()) PLACEHOLDER_DATA_NONE else account.email,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_ACCOUNT_PASSWORD,
                    PLACEHOLDER_DATA_PASSWORD_HIDDEN,
                    View.VISIBLE,
                    View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_ACCOUNT_WEBSITE,
                    if (account.website.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.website!!,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_ACCOUNT_2FA,
                    if (account.twoFA.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.twoFA!!
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_ACCOUNT_2FAKEYS,
                    if (account.twoFASecretKeys.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.twoFASecretKeys!!,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_ACCOUNT_ADDITIONAL_COMMENTS,
                    if (account.additionalInfo.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.additionalInfo!!
                )
            )
        }
}