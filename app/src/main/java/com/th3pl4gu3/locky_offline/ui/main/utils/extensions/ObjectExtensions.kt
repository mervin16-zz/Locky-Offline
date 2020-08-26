package com.th3pl4gu3.locky_offline.ui.main.utils.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.REGEX_CREDIT_CARD_AMEX
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.REGEX_CREDIT_CARD_DINNERSCLUB
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.REGEX_CREDIT_CARD_DISCOVER
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.REGEX_CREDIT_CARD_JCB
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.REGEX_CREDIT_CARD_MASTERCARD
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.REGEX_CREDIT_CARD_VISA
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.SETTINGS_CRYPTO_DIGEST_SCHEME
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/*
* Get type of credit card
* from the credit card number
*/
fun String.getCardType(): Card.CardType {

    val number = this.replace(",", "")

    return when {
        Regex(pattern = REGEX_CREDIT_CARD_VISA).containsMatchIn(number) -> {
            Card.CardType.VISA
        }
        Regex(pattern = REGEX_CREDIT_CARD_MASTERCARD).containsMatchIn(number) -> {
            Card.CardType.MASTERCARD
        }
        Regex(pattern = REGEX_CREDIT_CARD_AMEX).containsMatchIn(number) -> {
            Card.CardType.AMERICAN_EXPRESS
        }
        Regex(pattern = REGEX_CREDIT_CARD_DINNERSCLUB).containsMatchIn(number) -> {
            Card.CardType.DINNERS_CLUB
        }
        Regex(pattern = REGEX_CREDIT_CARD_DISCOVER).containsMatchIn(number) -> {
            Card.CardType.DISCOVER
        }
        Regex(pattern = REGEX_CREDIT_CARD_JCB).containsMatchIn(number) -> {
            Card.CardType.JCB
        }
        else -> {
            Card.CardType.DEFAULT
        }
    }
}

/*
* Re-formats a string to a
* credit card format
*/
fun String.toCreditCardFormat(): String {
    val number = this.replace(",", "").trim()
    val result = StringBuilder()

    number.indices.forEach { i ->
        if (i % 4 == 0 && i != 0) {
            result.append(" ")
        }
        result.append(number[i])
    }

    return result.toString()
}


/*
* Calendar formatting
*/
fun Calendar.toFormattedStringForCard(): String =
    SimpleDateFormat("MM/yy", Locale.ENGLISH).format(this.timeInMillis)

fun Calendar.toFormattedStringDefault(): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(this.timeInMillis)

fun String.toFormattedCalendarForCard(): Calendar {
    val sd = SimpleDateFormat("MM/yy", Locale.ENGLISH)
    val date = sd.parse(this)
    val cal = Calendar.getInstance()
    cal.time = date!!
    return cal
}

/*
* Styles a normal string and
* returns a spannable string
*/
fun String.setColor(color: Int): SpannableString {
    val spannable = SpannableString(this)
    spannable.setSpan(
        ForegroundColorSpan(color),
        0,
        this.length,
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
    )
    return spannable
}


/* Hash messages */
val String.hash: String
    get() = String(
        MessageDigest.getInstance(SETTINGS_CRYPTO_DIGEST_SCHEME)
            .digest(this.toByteArray())
    )
