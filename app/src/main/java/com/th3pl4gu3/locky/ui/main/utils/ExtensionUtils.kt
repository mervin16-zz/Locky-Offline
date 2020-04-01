package com.th3pl4gu3.locky.ui.main.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.Toast
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Card


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

fun Long.getCardType(): Card.CardType{

    val number = this.toString().replace(",", "")

    val ptVisa = "^4[0-9]{6,}$"
    val ptMasterCard = "^5[1-5][0-9]{5,}$"

    val ptAmeExp = "^3[47][0-9]{5,}$"

    val ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$"

    val ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$"

    val ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$"


    return when {
        number.matches(ptVisa.toRegex()) -> {
            Card.CardType.VISA
        }
        number.matches(ptMasterCard.toRegex()) -> {
            Card.CardType.MASTERCARD
        }
        number.matches(ptAmeExp.toRegex()) -> {
            Card.CardType.AMERICAN_EXPRESS
        }
        number.matches(ptDinClb.toRegex()) -> {
            Card.CardType.DINNERS_CLUB
        }
        number.matches(ptDiscover.toRegex()) -> {
            Card.CardType.DISCOVER
        }
        number.matches(ptJcb.toRegex()) -> {
            Card.CardType.JCB
        }
        else -> {
            Card.CardType.DEFAULT
        }
    }
}

fun Long.toCreditCardFormat(): String{
    val number = this.toString().replace(",", "")
    val result = StringBuilder()

    number.indices.forEach { i ->
        if (i % 4 == 0 && i != 0) {
            result.append(" ")
        }
        result.append(number[i])
    }

    return result.toString()
}