package com.th3pl4gu3.locky.ui.main.utils

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.repository.LoadingStatus


//TODO: Need to test all functions properly in BindingUtils.kt

/********************************* Card Binding Adapters****************************************/
@BindingAdapter("cardNumber")
fun TextView.setCardNumber(number: Long) {
    text = number.toCreditCardFormat()
}

@BindingAdapter("issuedCardDate", "expiryCardDate")
fun TextView.setCardDate(issued: String, expiry: String) {
    text = "Issued: $issued | Expiry: $expiry"
}

@BindingAdapter("cardLogo")
fun ImageView.setCardLogo(number: Long) {
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
    return if (account.username.isNotEmpty() && account.email.isNotEmpty()) {
        text = "${account.username} | ${account.email}"
    } else if (account.username.isNotEmpty()) {
        text = account.username
    } else {
        text = account.email
    }
}

@BindingAdapter("bindLogoImage")
fun ImageView.bindLogoImage(imgUrl: String) {
    imgUrl.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            .circleCrop()
            .placeholder(R.drawable.image_placeholder_logo)
            .into(this)
    }
}


/********************************* Other Binding Adapters****************************************/
@BindingAdapter("loadingStatusVisibility")
fun ProgressBar.setLoadingStatusVisibility(status: LoadingStatus) {
    visibility = when(status) {
        LoadingStatus.LOADING -> View.VISIBLE
        LoadingStatus.DONE, LoadingStatus.ERROR -> View.GONE
    }
}