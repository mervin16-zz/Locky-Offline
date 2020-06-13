package com.th3pl4gu3.locky_offline.core.main

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

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