package com.th3pl4gu3.locky.core.main

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "card_table")
data class Card(
    @PrimaryKey @ColumnInfo(name = "cardID") val cardID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "entryName") var entryName: String = "",
    @ColumnInfo(name = "number") var number: String = "",
    @ColumnInfo(name = "pin") var pin: String = "",
    @ColumnInfo(name = "bank") var bank: String = "",
    @ColumnInfo(name = "cardHolderName") var cardHolderName: String = "",
    @ColumnInfo(name = "issuedDate") var issuedDate: String = "01/10",
    @ColumnInfo(name = "expiryDate") var expiryDate: String = "01/12",
    @ColumnInfo(name = "moreInfo") var cardMoreInfo: String? = null
) : Parcelable {

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