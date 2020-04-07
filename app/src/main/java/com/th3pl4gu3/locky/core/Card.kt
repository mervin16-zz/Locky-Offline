package com.th3pl4gu3.locky.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Card(
    override val id: String,
    override var name: String = "",
    var number: Long = 0,
    var pin: Int? = null,
    var bank: String? = null,
    var cardHolderName: String = "",
    var issuedDate: Calendar = Calendar.getInstance(),
    var expiryDate: Calendar = Calendar.getInstance()
) : Credentials(), Parcelable {

    enum class CardType {
        VISA,
        MASTERCARD,
        AMERICAN_EXPRESS,
        JCB,
        DISCOVER,
        DINNERS_CLUB,
        DEFAULT
    }

}