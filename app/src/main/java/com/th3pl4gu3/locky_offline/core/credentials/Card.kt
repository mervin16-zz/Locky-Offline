package com.th3pl4gu3.locky_offline.core.credentials

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

/*
* The Card object holds data for bank cards
* i.e credit cards, debit cards, etc...
* Extends parent class Credentials
*/
@Parcelize
@Entity(tableName = "card_table")
data class Card(
    @ColumnInfo(name = "number") var number: String = "",
    @ColumnInfo(name = "pin") var pin: String = "",
    @ColumnInfo(name = "bank") var bank: String = "",
    @ColumnInfo(name = "cvc") var cvc: String = "",
    @ColumnInfo(name = "cardHolderName") var cardHolderName: String = "",
    @ColumnInfo(name = "issuedDate") var issuedDate: String = "01/10",
    @ColumnInfo(name = "expiryDate") var expiryDate: String = "01/12"
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