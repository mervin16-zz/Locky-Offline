package com.th3pl4gu3.locky_offline.core.main

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "account_table")
data class Account(
    @PrimaryKey @ColumnInfo(name = "accountID") val accountID: String = UUID.randomUUID()
        .toString(),
    @ColumnInfo(name = "accountName") var accountName: String = "",
    @ColumnInfo(name = "username") var username: String = "",
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "password") var password: String = "",
    @ColumnInfo(name = "logoUrl") var logoUrl: String = "",
    @ColumnInfo(name = "website") var website: String? = null,
    @ColumnInfo(name = "moreInfo") var accountMoreInfo: String? = null,
    @ColumnInfo(name = "authType") var authenticationType: String? = null,
    @ColumnInfo(name = "secretKeys") var twoFASecretKeys: String? = null
) : Parcelable