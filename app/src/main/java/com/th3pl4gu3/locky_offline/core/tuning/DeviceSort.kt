package com.th3pl4gu3.locky_offline.core.tuning

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
* The Device Sort object holds data for devices Sorting type
* User can switch between different types of sorting as per their preferences
* This data is stored here and is Parcelable
*/
@Parcelize
class DeviceSort(
    var entryName: Boolean = false,
    var username: Boolean = false,
    var ipAddress: Boolean = false
) : Parcelable {

    override fun toString(): String {
        return "en:$entryName|un:$username|ia:$ipAddress"
    }
}

