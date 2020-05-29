package com.th3pl4gu3.locky_offline.ui.main.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.snackbar.Snackbar
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.REGEX_CREDIT_CARD_AMEX
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.REGEX_CREDIT_CARD_DINNERSCLUB
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.REGEX_CREDIT_CARD_DISCOVER
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.REGEX_CREDIT_CARD_JCB
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.REGEX_CREDIT_CARD_MASTERCARD
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.REGEX_CREDIT_CARD_VISA
import java.text.SimpleDateFormat
import java.util.*

//TODO: Need to test all functions properly in ExtensionUtils.kt

fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, duration).show()

fun Window.activateLightStatusBar(view: View) {
    var flags: Int = view.systemUiVisibility
    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    view.systemUiVisibility = flags
    this.statusBarColor = Color.WHITE
}

fun Window.activateDarkStatusBar() {
    this.statusBarColor = context.getColor(R.color.colorPrimary)
}

fun String.getCardType(): Card.CardType {

    val number = this.replace(",", "")

    return when {
        Regex(pattern = REGEX_CREDIT_CARD_VISA).containsMatchIn(number) -> {
            Card.CardType.VISA
        }
        Regex(pattern = REGEX_CREDIT_CARD_MASTERCARD).containsMatchIn(number)  -> {
            Card.CardType.MASTERCARD
        }
        Regex(pattern = REGEX_CREDIT_CARD_AMEX).containsMatchIn(number)  -> {
            Card.CardType.AMERICAN_EXPRESS
        }
        Regex(pattern = REGEX_CREDIT_CARD_DINNERSCLUB).containsMatchIn(number)  -> {
            Card.CardType.DINNERS_CLUB
        }
        Regex(pattern = REGEX_CREDIT_CARD_DISCOVER).containsMatchIn(number)  -> {
            Card.CardType.DISCOVER
        }
        Regex(pattern = REGEX_CREDIT_CARD_JCB).containsMatchIn(number)  -> {
            Card.CardType.JCB
        }
        else -> {
            Card.CardType.DEFAULT
        }
    }
}

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

inline fun View.snackbar(message: String, length: Int = Snackbar.LENGTH_INDEFINITE, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, listener: (View) -> Unit) {
    setAction(action, listener)
}

fun Context.copyToClipboard(data: String)
        = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("text", data))

fun Context.createPopUpMenu(
    view: View,
    menu: Int,
    itemListener: PopupMenu.OnMenuItemClickListener,
    dismissListener: PopupMenu.OnDismissListener
) {
    val popup = PopupMenu(this, view)
    //inflating menu from xml resource
    popup.inflate(menu)
    //adding click & on dismiss listener
    popup.setOnMenuItemClickListener(itemListener)
    popup.setOnDismissListener(dismissListener)
    popup.show()
}

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

fun String.toFormattedCalendarDefault(): Calendar {
    val sd = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    val date = sd.parse(this)
    val cal = Calendar.getInstance()
    cal.time = date!!
    return cal
}

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun generateUniqueID(): String = UUID.randomUUID().toString()
