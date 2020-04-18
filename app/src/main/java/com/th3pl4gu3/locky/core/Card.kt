package com.th3pl4gu3.locky.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Card(
    val id: String = "",
    var name: String = "",
    var number: Long = 0,
    var pin: Short = 0,
    var bank: String = "",
    var cardHolderName: String = "",
    var issuedDate: Calendar = Calendar.getInstance(),
    var expiryDate: Calendar = Calendar.getInstance(),
    var additionalInfo: String? = null
) : Credentials(id = id, name = name, additionalInfo = additionalInfo), Parcelable {

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