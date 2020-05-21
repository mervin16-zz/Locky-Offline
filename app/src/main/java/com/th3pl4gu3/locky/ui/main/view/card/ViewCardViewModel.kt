package com.th3pl4gu3.locky.ui.main.view.card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.Card
import com.th3pl4gu3.locky.repository.database.CardDao
import com.th3pl4gu3.locky.ui.main.view.CredentialsField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewCardViewModel(application: Application) : AndroidViewModel(application) {

    internal fun delete(key: String) {
        viewModelScope.launch {
            deleteData(key)
        }
    }

    private suspend fun deleteData(key: String) {
        withContext(Dispatchers.IO) {
            CardDao().remove(key)
        }
    }

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
                    data = if (card.cardMoreInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else card.cardMoreInfo!!
                )
            )
        }

    private fun getString(res: Int) = getApplication<Application>().getString(res)
}