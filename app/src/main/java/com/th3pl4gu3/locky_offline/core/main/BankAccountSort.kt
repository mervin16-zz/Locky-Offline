package com.th3pl4gu3.locky_offline.core.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class BankAccountSort(
    var accountName: Boolean = false,
    var accountOwner: Boolean = false,
    var bank: Boolean = false
) : Parcelable {

    override fun toString(): String {
        return "an:$accountName|ao:$accountOwner|b:$bank"
    }
}

