package com.th3pl4gu3.locky_offline.core.tuning

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
* The Card Sort object holds data for Card Sorting type
* User can switch between different types of sorting as per their preferences
* This data is stored here and is Parcelable
*/
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

