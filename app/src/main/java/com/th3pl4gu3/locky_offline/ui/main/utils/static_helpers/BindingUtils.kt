package com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.*
import com.th3pl4gu3.locky_offline.repository.billing.BillingRepository
import com.th3pl4gu3.locky_offline.ui.main.LockyApplication
import com.th3pl4gu3.locky_offline.ui.main.utils.enums.MessageType
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.*
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.SettingsManager
import java.util.*

/*
* Binding to load an image
* from a given url.
* Manually specify the loading resource to show
* and the error resource to show if the loading fails
*/
@BindingAdapter("imageUrl", "loadingResource", "errorResource")
fun ImageView.imageUrl(imageUrl: String, loadingResource: Drawable, errorResource: Drawable) {
    imageUrl.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        loadImageUrl(imgUri, loadingResource, errorResource)
    }
}


/*
* Below is a set of unification UI
* that is used to display all locky credentials
* objects in a list.
*/
@BindingAdapter("configureLogo")
fun ImageView.configureLogo(credential: Credentials) {
    /* We check which locky credential has been transformed in a basic credential */
    when (credential) {
        is Account -> {
            /*
            * If it's Accounts
            * We need to load the logo url
            * No further modifications are allowed on the user level
            */
            credential.logoUrl.let {
                val imgUri = it.toUri().buildUpon()?.scheme("https")?.build()
                loadImageUrl(
                    imgUri,
                    ContextCompat.getDrawable(context, R.drawable.ic_image_loading),
                    ContextCompat.getDrawable(context, R.drawable.ic_account_placeholder)
                )
            }
        }
        is Card -> {
            /*
            * If it's Cards
            * We only need to determine
            * the card brand. No further modifications
            * are allowed on the user level.
            */
            setBackgroundResource(
                when (credential.number.getCardType()) {
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
        is BankAccount -> {
            /*
            * If it's Bank Account
            * We just need to load the default
            * bank icon as Bank Accounts doesn't allow
            * customization of logo
            * Then we load the accent color as bank accounts allow modification
            * of accent.
            */
            this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bank))
            this.setColorFilter(Color.parseColor(credential.accent))
        }
        is Device -> {
            /*
            * If it's Device
            * We need to load both the icon and accent as
            * device allows modification of both icon and accent.
            */
            this.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    resources.getIdentifier(credential.icon, "drawable", context.packageName)
                )
            )
            this.setColorFilter(Color.parseColor(credential.accent))
        }
    }
}

@BindingAdapter("setCredentialSubtitle")
fun TextView.setCredentialSubtitle(credential: Credentials) {
    when (credential) {
        is Account -> {
            /*
            * Here we check if the given subtitle is empty
            * If it's not, we display the login.
            * Else we display a text stating that
            * no login was provided
            */
            if (credential.username.isEmpty() && credential.email.isEmpty()) {
                text = resources.getString(R.string.field_account_blank_login)
                return
            }

            if (
                credential.username.isEmpty() ||
                credential.username.toLowerCase(Locale.ROOT) == resources.getString(R.string.field_placeholder_na) ||
                credential.username.toLowerCase(Locale.ROOT) == resources.getString(R.string.field_placeholder_empty)
            ) {
                text = resources.getString(R.string.app_user_login, credential.email)
                return
            }

            text = resources.getString(R.string.app_user_login, credential.username)
        }
        is Card -> {
            /*
            * If its cards, we just call the method
            * that will format the string to a credit card
             */
            text = credential.number.toCreditCardFormat()
        }
        is BankAccount -> {
            /*
            * If its Bank Account, we just call the method
            * that will initialize text
             */
            text = credential.accountNumber
        }
        is Device -> {
            /*
            * If its Device, we just call the method
            * that will initialize text
             */
            text = credential.username
        }
    }
}

@BindingAdapter("setCredentialOtherSubtitle")
fun TextView.setCredentialOtherSubtitle(credential: Credentials) {
    when (credential) {
        is Account -> {
            /*
            * Here we check if the given website is empty
            * If it's not, we display the website.
            * Else we display a text stating that
            * no website was provided
             */
            text = if (credential.website.isEmpty()) {
                resources.getString(R.string.field_account_blank_website)
            } else {
                credential.website
            }
        }
        is Card -> {
            /*
            * If it's cards, we just assign it to the card owner
            */
            text = credential.cardHolderName
        }
        is BankAccount -> {
            /*
            * If it's Bank Account, we just assign it to the account owner
            */
            text = credential.accountOwner
        }
        is Device -> {
            /*
            * If it's device, we just assign it to the ip address
            * We first need to check if it's not empty as
            * ip address is an optional field.
            */
            text = if (credential.ipAddress.isNullOrEmpty()) {
                resources.getString(R.string.field_device_blank_ip)
            } else {
                credential.ipAddress
            }
        }
    }
}

@BindingAdapter("listTitleMessageCardEligibility")
fun TextView.listTitleMessageCardEligibility(credential: Credentials) {
    /*
    * We check if the credential is a Card
    * and if this setting has been enabled
    */
    if (credential is Card && SettingsManager(LockyApplication.getInstance()).isCardExpirationEnabled()) {
        if (credential.hasExpired() || credential.expiringWithin30Days()) {
            this.setTextColor(ContextCompat.getColor(context, R.color.colorTextTitleMessage))
            return
        }
    }

    this.setTextColor(ContextCompat.getColor(context, R.color.colorTextTitle))
}

@BindingAdapter("credentialCardConfiguration")
fun MaterialCardView.credentialCardConfiguration(credential: Credentials) {
    /*
    * We check if the credential is a Card
    * and if this setting has been enabled
    */
    if (credential is Card && SettingsManager(LockyApplication.getInstance()).isCardExpirationEnabled()) {
        if (credential.hasExpired()) {
            this.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorInfoError_Light
                )
            )
            return
        }

        if (credential.expiringWithin30Days()) {
            this.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorInfoWarning_Light
                )
            )
            return
        }

    }

    this.setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
}

/*
* Binding to format the "Member since"
* text from the profile screen
*/
@BindingAdapter("memberSince")
fun TextView.memberSince(dateJoined: String) {
    this.text = resources.getString(R.string.app_user_member_since, dateJoined)
}

/*
* Binding to loading donation
* icons to its correct SkuDetails
*/
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

/*
* Configures the card logo
* as per changes to the card number
*/
@BindingAdapter("setCardLogo")
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

/*
* Shows a message for cards
* whether it has been expired or is
* about to expired. Styles the message accordingly
*/
@BindingAdapter("lockyMessageParams")
fun TextView.lockyMessageParams(messageType: MessageType) {
    when (messageType) {
        MessageType.WARNING -> {
            this.text = resources.getString(R.string.message_card_warning_willExpire)
            this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorInfoWarning_Light))
        }
        MessageType.ERROR -> {
            this.text = resources.getString(R.string.message_card_error_expired)
            this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorInfoError_Light))
        }
        else -> return
    }
}

/*
* Loads a default icon
*/
@BindingAdapter("loadIcon")
fun ImageView.loadIcon(icon: Drawable) {
    this.setImageDrawable(icon)
}

/*
* Apply a filter accent to an icon
*/
@BindingAdapter("iconColor")
fun ImageView.iconColor(hexColor: String) {
    if (hexColor.isNotEmpty()) {
        this.setColorFilter(Color.parseColor(hexColor))
    }
}

/*
* Load an icon as per the
* icon resource name
*/
@BindingAdapter("loadCustomIcon")
fun ImageView.loadCustomIcon(icon: String) {
    this.setImageDrawable(
        ContextCompat.getDrawable(
            context,
            resources.getIdentifier(icon, "drawable", context.packageName)
        )
    )
}

/*
* Determines if the show password
* icon should be visible or not
*/
@BindingAdapter("isSimplified", "credential")
fun ImageButton.determineViewSecretVisibility(isSimplified: Boolean, credential: Credentials) =
    if (isSimplified || credential is BankAccount) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
    }