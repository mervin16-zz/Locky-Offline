package com.th3pl4gu3.locky_offline.core.main

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "bank_account_table")
data class BankAccount(
    @PrimaryKey @ColumnInfo(name = "id") val bankAccountID: String = UUID.randomUUID()
        .toString(),
    @ColumnInfo(name = "entryName") var entryName: String = "",
    @ColumnInfo(name = "accountNumber") var accountNumber: String = "",
    @ColumnInfo(name = "accountOwner") var accountOwner: String = "",
    @ColumnInfo(name = "bank") var bank: String = "",
    @ColumnInfo(name = "iban") var iban: String? = null,
    @ColumnInfo(name = "swiftCode") var swiftCode: String? = null,
    @ColumnInfo(name = "userID") var user: String = ""
) : Parcelable