package com.th3pl4gu3.locky_offline.ui.main.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.*
import com.th3pl4gu3.locky_offline.repository.database.repositories.AccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.CardRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.DeviceRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.enums.MessageType
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.expiringWithin30Days
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.getString
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.hasExpired
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.SettingsManager
import kotlinx.coroutines.launch

class CredentialViewModel(application: Application) : AndroidViewModel(application) {

    /* Private Variables */
    private val _cardMessageType = MutableLiveData(MessageType.NONE)

    /* Properties */
    val cardMessageType: LiveData<MessageType>
        get() = _cardMessageType

    /*
    * Accessible Functions
    */
    internal fun updateMessageType(credentials: Credentials) {
        if (credentials is Card) {
            /* First check if this setting has been enabled */
            if (SettingsManager(getApplication()).isCardExpirationEnabled()) {
                with(credentials) {
                    if (hasExpired()) {
                        _cardMessageType.value = MessageType.ERROR
                        return
                    }

                    if (expiringWithin30Days()) {
                        _cardMessageType.value = MessageType.WARNING
                        return
                    }
                }
            }
        }

        _cardMessageType.value = MessageType.NONE
    }

    internal fun delete(credentials: Credentials) = viewModelScope.launch {
        when (credentials) {
            is Account -> AccountRepository.getInstance(getApplication()).delete(credentials.id)
            is BankAccount -> BankAccountRepository.getInstance(getApplication())
                .delete(credentials.id)
            is Card -> CardRepository.getInstance(getApplication()).delete(credentials.id)
            is Device -> DeviceRepository.getInstance(getApplication()).delete(credentials.id)
        }
    }

    // list for the fields
    internal fun accountFields(account: Account): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            with(account) {
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_account_username),
                        data = if (username.isEmpty()) getString(R.string.field_placeholder_empty) else username,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_account_email),
                        data = if (email.isEmpty()) getString(R.string.field_placeholder_empty) else email,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_account_password_wr),
                        data = if (password.isEmpty()) getString(R.string.field_placeholder_empty) else password,
                        isViewable = true,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_account_website),
                        data = if (website.isEmpty()) getString(R.string.field_placeholder_empty) else website,
                        isLinkable = true,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_account_authentication_type),
                        data = if (authenticationType.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else authenticationType!!
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_account_2fakeys),
                        data = if (twoFASecretKeys.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else twoFASecretKeys!!,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_account_additional),
                        data = if (additionalInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else additionalInfo!!
                    )
                )
            }
        }

    internal fun bankAccountFields(bankAccount: BankAccount): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            with(bankAccount) {
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

    internal fun deviceFields(device: Device): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            with(device) {
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_device_username),
                        data = if (username.isEmpty()) getString(R.string.field_placeholder_empty) else username,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_device_password),
                        data = if (password.isEmpty()) getString(R.string.field_placeholder_empty) else password,
                        isViewable = true,
                        isShareable = true,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_device_ip),
                        data = if (ipAddress.isNullOrBlank()) getString(R.string.field_placeholder_empty) else ipAddress!!,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_device_mac),
                        data = if (macAddress.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else macAddress!!,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_device_additional),
                        data = if (additionalInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else additionalInfo!!
                    )
                )
            }
        }

    internal fun cardFields(card: Card): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            with(card) {
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_card_number),
                        data = if (number.isEmpty()) getString(R.string.field_placeholder_empty) else number,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_card_bank),
                        data = if (bank.isEmpty()) getString(R.string.field_placeholder_empty) else bank,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_card_pin),
                        data = if (pin.isEmpty()) getString(R.string.field_placeholder_empty) else pin,
                        isCopyable = true,
                        isViewable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_card_cvc),
                        data = cvc,
                        isCopyable = true,
                        isViewable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_card_holder),
                        data = cardHolderName,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_card_date_issued),
                        data = issuedDate,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_card_date_expiry),
                        data = expiryDate,
                        isCopyable = true
                    )
                )
                add(
                    CredentialsField(
                        subtitle = getString(R.string.field_card_additional),
                        data = if (additionalInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else additionalInfo!!
                    )
                )
            }
        }
}