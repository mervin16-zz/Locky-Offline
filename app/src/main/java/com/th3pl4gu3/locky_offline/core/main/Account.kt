package com.th3pl4gu3.locky_offline.core.main

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "account_table")
data class Account(
    @ColumnInfo(name = "username") var username: String = "",
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "password") var password: String = "",
    @ColumnInfo(name = "logoUrl") var logoUrl: String = "",
    @ColumnInfo(name = "website") var website: String = "",
    @ColumnInfo(name = "authType") var authenticationType: String? = null,
    @ColumnInfo(name = "secretKeys") var twoFASecretKeys: String? = null
) : Credentials(), Parcelable