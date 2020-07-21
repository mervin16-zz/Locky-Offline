package com.th3pl4gu3.locky_offline.ui.main.view.bank_account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.credentials.BankAccount
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.ui.main.view.CredentialsField
import kotlinx.coroutines.launch

class ViewBankAccountViewModel(application: Application) : AndroidViewModel(application) {

    /*
    * Accessible functions
    */
    internal fun fieldList(account: BankAccount): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            with(account) {
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_bank_account_number),
                        data = if (accountNumber.isEmpty()) getString(R.string.field_placeholder_empty) else accountNumber,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_bank_account_owner),
                        data = if (accountOwner.isEmpty()) getString(R.string.field_placeholder_empty) else accountOwner,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_bank_account_bank),
                        data = if (bank.isEmpty()) getString(R.string.field_placeholder_empty) else bank,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_bank_account_iban),
                        data = if (iban.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else iban!!,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_bank_account_swift),
                        data = if (swiftCode.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else swiftCode!!,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_bank_account_additional),
                        data = if (additionalInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else additionalInfo!!
                    )
                )
            }
        }


    /*
    * In-accessible functions
    */
    internal fun delete(key: Int) {
        viewModelScope.launch {
            BankAccountRepository.getInstance(getApplication()).delete(key)
        }
    }

    private fun getString(res: Int) = getApplication<Application>().getString(res)
}