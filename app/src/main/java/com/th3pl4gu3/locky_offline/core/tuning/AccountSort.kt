package com.th3pl4gu3.locky_offline.core.tuning

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
* The Account Sort object holds data for Account Sorting type
* User can switch between different types of sorting as per their preferences
* This data is stored here and is Parcelable
*/
@Parcelize
class AccountSort(
    var name: Boolean = false,
    var username: Boolean = false,
    var email: Boolean = false,
    var website: Boolean = false,
    var authType: Boolean = false
) : Parcelable {

    override fun toString(): String {
        return "nm:$name|un:$username|em:$email|ws:$website|at:$authType"
    }
}

