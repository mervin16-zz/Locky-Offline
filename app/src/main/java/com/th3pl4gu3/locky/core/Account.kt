package com.th3pl4gu3.locky.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(
   override val id: String,
   override var name: String = "",
   var username: String? = null,
   var email: String? = null,
   var password: String = "",
   var website: String? = null,
   var isTwoFA: Boolean = false,
   var twoFASecretKeys: String? = null
) : Credentials(), Parcelable