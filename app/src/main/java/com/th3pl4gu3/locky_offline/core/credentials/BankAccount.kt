package com.th3pl4gu3.locky_offline.core.credentials

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

/*
* The Bank Account object holds data for User's bank accounts
* Any bank's accounts can be used. This is generic.
* Extends parent class Credentials
*/
@Parcelize
@Entity(tableName = "bank_account_table")
data class BankAccount(
    @ColumnInfo(name = "accountNumber") var accountNumber: String = "",
    @ColumnInfo(name = "accountOwner") var accountOwner: String = "",
    @ColumnInfo(name = "bank") var bank: String = "",
    @ColumnInfo(name = "accent") var accent: String = "#D35C4B6C",
    @ColumnInfo(name = "iban") var iban: String? = null,
    @ColumnInfo(name = "swiftCode") var swiftCode: String? = null
) : Credentials(), Parcelable