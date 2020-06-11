package com.th3pl4gu3.locky_offline.core.main

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "card_table")
data class Card(
    @ColumnInfo(name = "number") var number: String = "",
    @ColumnInfo(name = "pin") var pin: String = "",
    @ColumnInfo(name = "bank") var bank: String = "",
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