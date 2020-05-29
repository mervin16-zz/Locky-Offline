package com.th3pl4gu3.locky_offline.core.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CardSort(
    var sortByName: Boolean = false,
    var sortByType: Boolean = false,
    var sortByBank: Boolean = false,
    var sortByCardHolderName: Boolean = false
) : Parcelable {

    override fun toString(): String {
        return "n:$sortByName|t:$sortByType|b:$sortByBank|c:$sortByCardHolderName"
    }
}

