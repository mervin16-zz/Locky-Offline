package com.th3pl4gu3.locky.ui.main.view.card

import android.app.Application
import android.view.View
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
                    getString(R.string.field_card_name),
                    if (card.entryName.isEmpty()) getString(R.string.field_placeholder_empty) else card.entryName,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_card_bank),
                    if (card.bank.isEmpty()) getString(R.string.field_placeholder_empty) else card.bank,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_card_pin),
                    getString(R.string.field_placeholder_hidden),
                    View.VISIBLE,
                    View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_card_holder),
                    card.cardHolderName,
                    View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_card_date_issued),
                    card.issuedDate,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_card_date_expiry),
                    card.expiryDate,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    getString(R.string.field_card_additional),
                    if (card.cardMoreInfo.isNullOrEmpty()) getString(R.string.field_placeholder_empty) else card.cardMoreInfo!!
                )
            )
        }

    private fun getString(res: Int) = getApplication<Application>().getString(res)
}