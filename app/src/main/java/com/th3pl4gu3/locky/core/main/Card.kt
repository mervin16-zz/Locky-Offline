package com.th3pl4gu3.locky.core.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
    var cardID: String = "",
    var userID: String = "",
    var entryName: String = "",
    var number: String = "",
    var pin: String = "",
    var bank: String = "",
    var cardHolderName: String = "",
    var issuedDate: String = "01/10",
    var expiryDate: String = "01/12",
    var cardMoreInfo: String? = null
) : Credentials(id = cardID, user = userID, name = entryName, additionalInfo = cardMoreInfo),
    Parcelable {

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