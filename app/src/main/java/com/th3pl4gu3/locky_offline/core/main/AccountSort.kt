package com.th3pl4gu3.locky_offline.core.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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

