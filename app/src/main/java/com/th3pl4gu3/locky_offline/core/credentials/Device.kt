package com.th3pl4gu3.locky_offline.core.credentials

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

/*
* The Device object holds data for your
* personal devices credentials
* i.e home router, computer, smart devices etc
* Extends parent class Credentials
*/
@Parcelize
@Entity(tableName = "device_table")
data class Device(
    @ColumnInfo(name = "icon") var icon: String = "ic_device",
    @ColumnInfo(name = "username") var username: String = "",
    @ColumnInfo(name = "password") var password: String = "",
    @ColumnInfo(name = "accent") var accent: String = "#D35C4B6C",
    @ColumnInfo(name = "ipAddress") var ipAddress: String? = null,
    @ColumnInfo(name = "macAddress") var macAddress: String? = null
) : Credentials(), Parcelable