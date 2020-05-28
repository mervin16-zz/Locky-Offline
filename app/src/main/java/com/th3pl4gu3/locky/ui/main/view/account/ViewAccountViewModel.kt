package com.th3pl4gu3.locky.ui.main.view.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.Account
import com.th3pl4gu3.locky.repository.database.AccountRepository
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
            /*AccountRepository(getApplication()).delete(key)*/
            TODO("Fix")
        }
    }

    internal fun fieldList(account: Account): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_account_username),
                    data = if (account.username.isEmpty()) getString(R.string.field_placeholder_empty) else account.username,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_account_email),
                    data = if (account.email.isEmpty()) getString(R.string.field_placeholder_empty) else account.email,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_account_password),
                    data = if (account.password.isEmpty()) getString(R.string.field_placeholder_empty) else account.password,
                    isViewable = true,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_account_website),
                    data = if (account.website.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else account.website!!,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_account_authentication_type),
                    data = if (account.authenticationType.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else account.authenticationType!!
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_account_2fakeys),
                    data = if (account.twoFASecretKeys.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else account.twoFASecretKeys!!,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_account_additional),
                    data = if (account.accountMoreInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else account.accountMoreInfo!!
                )
            )
        }


    private fun getString(res: Int) = getApplication<Application>().getString(res)
}