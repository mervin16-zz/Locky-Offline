package com.th3pl4gu3.locky_offline.ui.main.utils

import android.app.Activity
import android.app.SearchManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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

const val OFFSET_Y_TOAST = 40
const val OFFSET_X_TOAST = 0

/*
* Toast functions
* - can call directly from fragment
*/
fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    with(Toast.makeText(requireContext(), text, duration)) {
        setGravity(Gravity.BOTTOM, OFFSET_X_TOAST, OFFSET_Y_TOAST)
        show()
    }
}

fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    with(Toast.makeText(this, text, duration)) {
        setGravity(Gravity.BOTTOM, OFFSET_X_TOAST, OFFSET_Y_TOAST)
        show()
    }
}

/*
* Credit card helpers
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
* SnackBar helpers
*/
inline fun View.snackbar(
    message: String,
    length: Int = Snackbar.LENGTH_INDEFINITE,
    f: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, listener: (View) -> Unit) {
    setAction(action, listener)
}

/*
* Copy to clipboard
*/
fun Fragment.copyToClipboard(data: String) =
    (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
        ClipData.newPlainText(
            "text",
            data
        )
    )

/*
* Popup menu
*/
fun Fragment.createPopUpMenu(
    view: View,
    menu: Int,
    itemListener: PopupMenu.OnMenuItemClickListener,
    dismissListener: PopupMenu.OnDismissListener
) {
    val popup = PopupMenu(requireContext(), view)
    //inflating menu from xml resource
    popup.inflate(menu)
    //adding click & on dismiss listener
    popup.setOnMenuItemClickListener(itemListener)
    popup.setOnDismissListener(dismissListener)
    popup.show()
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
* Open an activity
*/
fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}


/*
* Checks if connected to the internet
*/
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

/*
* JetPack navigation made easy
*/
fun Activity.navigateTo(destination: Int) {
    this.findNavController(R.id.Navigation_Host).navigate(destination)
}

fun Fragment.navigateTo(directions: NavDirections) {
    this.findNavController().navigate(directions)
}

/*
* Hides soft keyboard
*/
fun Fragment.hideSoftKeyboard(rootView: View) {
    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(rootView.windowToken, 0)
}


/*
* Load an image from URL
*/
fun ImageView.loadImageUrl(uri: Uri, loadingResource: Drawable, errorResource: Drawable) {
    Glide.with(this.context)
        .load(uri)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .circleCrop()
        .placeholder(loadingResource)
        .error(errorResource)
        .into(this)
}


/*
* Intents facilities
*/
fun openUrl(url: String) = Intent(Intent.ACTION_WEB_SEARCH).apply {
    putExtra(SearchManager.QUERY, url)
}

fun openMail(recipient: Array<String>, subject: String) = Intent(Intent.ACTION_SENDTO).apply {
    data = Uri.parse("mailto:")
    putExtra(
        Intent.EXTRA_EMAIL,
        recipient
    ) // recipients
    putExtra(Intent.EXTRA_SUBJECT, subject)
}

fun share(message: String) = Intent(Intent.ACTION_SEND).apply {
    putExtra(
        Intent.EXTRA_TEXT,
        message
    )
    type = "text/plain"
}


/*
* Others
*/
fun generateUniqueID(): String = UUID.randomUUID().toString()
