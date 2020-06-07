package com.th3pl4gu3.locky_offline.ui.main.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.repository.billing.BillingRepository
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


/********************************* Donations Binding Adapters****************************************/
@BindingAdapter("loadDonationIcon")
fun ImageView.loadDonationIcon(donationID: String) {
    when (donationID) {
        BillingRepository.COOKIE -> this.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_cookie,
                null
            )
        )
        BillingRepository.MILKSHAKE -> this.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_soda,
                null
            )
        )
        BillingRepository.SANDWICH -> this.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_sandwich,
                null
            )
        )
        BillingRepository.BURGER -> this.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_burger,
                null
            )
        )
        BillingRepository.GIFT -> this.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_gift,
                null
            )
        )
        BillingRepository.STAR -> this.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_rate,
                null
            )
        )
        else -> return
    }
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