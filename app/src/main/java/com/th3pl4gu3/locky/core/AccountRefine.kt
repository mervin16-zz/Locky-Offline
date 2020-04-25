package com.th3pl4gu3.locky.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountRefine(
    var website: Boolean = false,
    var email: Boolean = false,
    var twofa: Boolean = false
) : Parcelable

