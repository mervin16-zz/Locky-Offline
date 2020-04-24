package com.th3pl4gu3.locky.ui.main.view.card

import android.view.View
import androidx.lifecycle.ViewModel
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_CARD_ADDITIONAL_COMMENTS
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_CARD_BANK
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_CARD_CARD_HOLDER
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_CARD_EXPIRY
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_CARD_ISSUED
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_CARD_NAME
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.LABEL_TEXTBOX_CARD_PIN
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.PLACEHOLDER_DATA_NONE
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.PLACEHOLDER_DATA_PASSWORD_HIDDEN
import com.th3pl4gu3.locky.ui.main.utils.toFormattedString
import com.th3pl4gu3.locky.ui.main.view.CredentialsField

class ViewCardViewModel : ViewModel() {

    internal fun fieldList(card: Card): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(
                CredentialsField(
                    LABEL_TEXTBOX_CARD_NAME,
                    if (card.name.isEmpty()) PLACEHOLDER_DATA_NONE else card.name,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_CARD_BANK,
                    if (card.bank.isEmpty()) PLACEHOLDER_DATA_NONE else card.bank,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_CARD_PIN,
                    PLACEHOLDER_DATA_PASSWORD_HIDDEN,
                    View.VISIBLE,
                    View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_CARD_CARD_HOLDER,
                    card.cardHolderName,
                    View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_CARD_ISSUED,
                    card.issuedDate,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_CARD_EXPIRY,
                    card.expiryDate,
                    isCopyable = View.VISIBLE
                )
            )
            add(
                CredentialsField(
                    LABEL_TEXTBOX_CARD_ADDITIONAL_COMMENTS,
                    if (card.additionalInfo.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else card.additionalInfo!!
                )
            )
        }
}