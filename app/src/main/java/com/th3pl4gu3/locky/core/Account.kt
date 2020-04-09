package com.th3pl4gu3.locky.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(
   override val id: String,
   override var name: String = "",
   var username: String = "",
   var email: String = "",
   var password: String = "",
   var website: String? = null,
   var twoFA: String? = null,
   var twoFASecretKeys: String? = null
) : Credentials(), Parcelable