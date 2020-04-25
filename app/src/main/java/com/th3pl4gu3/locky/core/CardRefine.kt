package com.th3pl4gu3.locky.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardRefine(
    var cardType: Boolean = false,
    var bank: Boolean = false,
    var cardHolderName: Boolean = false
) : Parcelable

