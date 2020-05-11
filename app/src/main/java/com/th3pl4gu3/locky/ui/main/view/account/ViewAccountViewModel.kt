package com.th3pl4gu3.locky.ui.main.view.account

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.Account
import com.th3pl4gu3.locky.repository.database.AccountDao
import com.th3pl4gu3.locky.ui.main.view.CredentialsField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewAccountViewModel(application: Application) : AndroidViewModel(application) {

    internal fun delete(key: String) {
        viewModelScope.launch {
            deleteAccount(key)
        }
    }

    private suspend fun deleteAccount(key: String) {
        withContext(Dispatchers.IO) {
            AccountDao().remove(key)
        }
    }

    internal fun fieldList(account: Account): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(
                CredentialsField(
                    getString(R.string.field_account_username),
                    if (account.username.isEmpty()) getString(R.string.field_placeholder_empty) else account.username,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_account_email),
                    if (account.email.isEmpty()) getString(R.string.field_placeholder_empty) else account.email,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_account_password),
                    getString(R.string.field_placeholder_hidden),
                    View.VISIBLE,
                    View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_account_website),
                    if (account.website.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else account.website!!,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_account_authentication_type),
                    if (account.authenticationType.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else account.authenticationType!!
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_account_2fakeys),
                    if (account.twoFASecretKeys.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else account.twoFASecretKeys!!,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_account_additional),
                    if (account.accountMoreInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else account.accountMoreInfo!!
                )
            )
        }


    private fun getString(res: Int) = getApplication<Application>().getString(res)
}