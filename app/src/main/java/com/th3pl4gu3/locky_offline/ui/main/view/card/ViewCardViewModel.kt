package com.th3pl4gu3.locky_offline.ui.main.view.card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.repository.database.repositories.CardRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.expiringWithin30Days
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.hasExpired
import com.th3pl4gu3.locky_offline.ui.main.view.CredentialsField
import kotlinx.coroutines.launch

class ViewCardViewModel(application: Application) : AndroidViewModel(application) {

    /* Enum */
    enum class MessageType { NONE, ERROR, WARNING }

    /* Private Variables */
    private val _messageType = MutableLiveData(MessageType.NONE)

    /* Properties */
    val messageType: LiveData<MessageType>
        get() = _messageType

    /*
    * Accessible Functions
    */
    internal fun fieldList(card: Card): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_card_name),
                    data = if (card.entryName.isEmpty()) getString(R.string.field_placeholder_empty) else card.entryName,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_card_bank),
                    data = if (card.bank.isEmpty()) getString(R.string.field_placeholder_empty) else card.bank,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_card_pin),
                    data = if (card.pin.isEmpty()) getString(R.string.field_placeholder_empty) else card.pin,
                    isCopyable = true,
                    isViewable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_card_holder),
                    data = card.cardHolderName,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_card_date_issued),
                    data = card.issuedDate,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_card_date_expiry),
                    data = card.expiryDate,
                    isCopyable = true
                )
            )
            add(
                CredentialsField(
                    subtitle = getString(R.string.field_card_additional),
                    data = if (card.additionalInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else card.additionalInfo!!
                )
            )
        }

    internal fun updateMessageType(card: Card) =
        with(card) {
            if (hasExpired()) {
                _messageType.value = MessageType.ERROR
                return@with
            }

            if (expiringWithin30Days()) {
                _messageType.value = MessageType.WARNING
                return@with
            }

            _messageType.value = MessageType.NONE
        }

    internal fun delete(key: Int) {
        viewModelScope.launch {
            CardRepository.getInstance(getApplication()).delete(key)
        }
    }

    /*
    * In-accessible Functions
    */
    private fun getString(res: Int) = getApplication<Application>().getString(res)
}