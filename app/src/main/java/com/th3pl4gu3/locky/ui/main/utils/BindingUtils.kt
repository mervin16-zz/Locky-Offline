package com.th3pl4gu3.locky.ui.main.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Card

@BindingAdapter("cardNumber")
fun TextView.setCardNumber(number: Long) {
    text = number.toCreditCardFormat()
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

@BindingAdapter("imageUrl")
fun ImageView.bindImage(imgUrl: String?){
    val completeUrl = "https://logo.clearbit.com/$imgUrl"
    completeUrl.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            .placeholder(R.drawable.image_placeholder_logo)
            .into(this)
    }
}