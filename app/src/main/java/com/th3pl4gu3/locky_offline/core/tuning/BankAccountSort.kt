package com.th3pl4gu3.locky_offline.core.tuning

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
* The Bank Account Sort object holds data for Bank Account Sorting type
* User can switch between different types of sorting as per their preferences
* This data is stored here and is Parcelable
*/
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

