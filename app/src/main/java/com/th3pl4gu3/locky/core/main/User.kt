package com.th3pl4gu3.locky.core.main

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.th3pl4gu3.locky.ui.main.utils.toFormattedStringDefault
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey @ColumnInfo(name = "userID") val userID: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "photo") var photo: String = "",
    @ColumnInfo(name = "email") var email: String = "",
    @ColumnInfo(name = "dateJoined") val dateJoined: String = Calendar.getInstance()
        .toFormattedStringDefault()
) : Parcelable

