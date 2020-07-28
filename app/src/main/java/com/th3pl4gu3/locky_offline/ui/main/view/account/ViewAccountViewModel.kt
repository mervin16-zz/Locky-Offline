package com.th3pl4gu3.locky_offline.ui.main.view.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.credentials.Account
import com.th3pl4gu3.locky_offline.repository.database.repositories.AccountRepository
import com.th3pl4gu3.locky_offline.ui.main.view.CredentialsField
import kotlinx.coroutines.launch

class ViewAccountViewModel(application: Application) : AndroidViewModel(application) {


    /*
    * Accessible functions
    */
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
                    data = if (account.website.isEmpty()) getString(R.string.field_placeholder_empty) else account.website,
                    isLinkable = true,
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
                    data = if (account.additionalInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else account.additionalInfo!!
                )
            )
        }


    /*
    * In-accessible functions
    */
    internal fun delete(key: Int) {
        viewModelScope.launch {
            AccountRepository.getInstance(getApplication()).delete(key)
        }
    }

    private fun getString(res: Int) = getApplication<Application>().getString(res)
}