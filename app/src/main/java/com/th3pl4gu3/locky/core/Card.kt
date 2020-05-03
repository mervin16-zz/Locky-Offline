package com.th3pl4gu3.locky.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
    override var id: String = "",
    var user: String = "",
    var name: String = "",
    var number: Long = 0,
    var pin: Int = 0,
    var bank: String = "",
    var cardHolderName: String = "",
    var issuedDate: String = "01/10",
    var expiryDate: String = "01/12",
    var additionalInfo: String? = null
) : Credentials(id = id, user = user, name = name, additionalInfo = additionalInfo), Parcelable {

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