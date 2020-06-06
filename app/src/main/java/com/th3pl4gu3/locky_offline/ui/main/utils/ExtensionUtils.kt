package com.th3pl4gu3.locky_offline.ui.main.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.REGEX_CREDIT_CARD_AMEX
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.REGEX_CREDIT_CARD_DINNERSCLUB
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.REGEX_CREDIT_CARD_DISCOVER
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.REGEX_CREDIT_CARD_JCB
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.REGEX_CREDIT_CARD_MASTERCARD
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.REGEX_CREDIT_CARD_VISA
import java.text.SimpleDateFormat
import java.util.*

fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, duration).show()

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

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun generateUniqueID(): String = UUID.randomUUID().toString()

fun Activity.isOnline(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

fun Activity.navigateTo(destination: Int) {
    this.findNavController(R.id.Navigation_Host).navigate(destination)
}

fun Fragment.navigateTo(directions: NavDirections) {
    this.findNavController().navigate(directions)
}

fun Activity.hideSoftKeyboard(rootView: View) {
    (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(rootView.windowToken, 0)
}
