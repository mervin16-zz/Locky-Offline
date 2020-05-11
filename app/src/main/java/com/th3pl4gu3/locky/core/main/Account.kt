package com.th3pl4gu3.locky.core.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(
    var accountID: String = "",
    var userID: String = "",
    var accountName: String = "",
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var logoUrl: String = "",
    var website: String? = null,
    var accountMoreInfo: String? = null,
    var authenticationType: String? = null,
    var twoFASecretKeys: String? = null
) : Credentials(
    id = accountID,
    user = userID,
    name = accountName,
    additionalInfo = accountMoreInfo
), Parcelable