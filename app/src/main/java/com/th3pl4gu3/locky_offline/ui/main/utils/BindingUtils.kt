package com.th3pl4gu3.locky_offline.ui.main.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.Card
import java.util.*

/********************************* Card Binding Adapters****************************************/
@BindingAdapter("cardNumber")
fun TextView.setCardNumber(number: String) {
    text = number.toCreditCardFormat()
}

@BindingAdapter("cardLogo")
fun ImageView.setCardLogo(number: String) {
    setBackgroundResource(
        when (number.getCardType()) {
            Card.CardType.VISA -> R.drawable.image_card_vs
            Card.CardType.MASTERCARD -> R.drawable.image_card_mc
            Card.CardType.AMERICAN_EXPRESS -> R.drawable.image_card_amex
            Card.CardType.DINNERS_CLUB -> R.drawable.image_card_dc
            Card.CardType.JCB -> R.drawable.image_card_jcb
            Card.CardType.DISCOVER -> R.drawable.image_card_disc
            Card.CardType.DEFAULT -> R.drawable.image_card_def
        }

    )
}


/********************************* Account Binding Adapters****************************************/
@BindingAdapter("accountLogin")
fun TextView.accountLogin(account: Account) {

    if (account.username.isEmpty() && account.email.isEmpty()) {
        text = resources.getString(R.string.field_account_blank_login)
        return
    }

    if (account.username.isNotEmpty() &&
        !(account.username.toLowerCase(Locale.ROOT) == resources.getString(R.string.field_placeholder_na) || account.username.toLowerCase(
            Locale.ROOT
        ) == resources.getString(R.string.field_placeholder_empty))
    ) {
        text = resources.getString(R.string.app_user_username, account.username)
        return
    }

    text = resources.getString(R.string.app_user_email, account.email)
}


/********************************* Profile Binding Adapters****************************************/
@BindingAdapter("memberSince")
fun TextView.memberSince(dateJoined: String) {
    this.text = resources.getString(R.string.app_user_member_since, dateJoined)
}


/********************************* Other Binding Adapters****************************************/

@BindingAdapter("imageUrl", "loadingResource", "errorResource")
fun ImageView.imageUrl(imageUrl: String, loadingResource: Drawable, errorResource: Drawable) {
    imageUrl.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        loadImageUrl(imgUri, loadingResource, errorResource)
    }
}

@BindingAdapter("loadIcon")
fun ImageView.loadIcon(icon: Drawable) {
    this.setImageDrawable(icon)
}