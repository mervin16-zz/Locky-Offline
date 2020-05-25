package com.th3pl4gu3.locky.ui.main.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.Account
import com.th3pl4gu3.locky.core.main.Card
import java.util.*


//TODO: Need to test all functions properly in BindingUtils.kt

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
    return if (account.username.isNotEmpty() && !(account.username.toLowerCase(Locale.ROOT) == "n/a" || account.username.toLowerCase(
            Locale.ROOT
        ) == "none")
    ) {
        text = resources.getString(R.string.app_user_username, account.username)
    } else {
        text = resources.getString(R.string.app_user_email, account.email)
    }
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
        Glide.with(this.context)
            .load(imgUri)
            .circleCrop()
            .placeholder(loadingResource)
            .error(errorResource)
            .into(this)
    }
}

@BindingAdapter("loadIcon")
fun ImageView.loadIcon(icon: Drawable) {
    this.setImageDrawable(icon)
}