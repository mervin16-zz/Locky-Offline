package com.th3pl4gu3.locky.core

import java.util.*

data class Card(override val id: String) : Credentials(){

    enum class CardType{
        VISA,
        MASTERCARD,
        AMERICAN_EXPRESS,
        JCB,
        DISCOVER,
        DINNERS_CLUB,
        DEFAULT
    }

    var number: Long = 0
    var cardHolderName: String = ""
    var issuedDate: Calendar = Calendar.getInstance()
    var expiryDate = Calendar.getInstance()
    var pin: Int? = null
    var bank: String? = null
}