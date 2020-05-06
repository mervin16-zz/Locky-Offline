package com.th3pl4gu3.locky.core.main

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class CardSort(
    var sortByName: Boolean = false,
    var sortByType: Boolean = false,
    var sortByBank: Boolean = false,
    var sortByCardHolderName: Boolean = false
) : Parcelable {

    @IgnoredOnParcel
    private val previousRefinements: String

    init {
        previousRefinements = this.toString()
    }

    override fun toString(): String {
        return "n:$sortByName|t:$sortByType|b:$sortByBank|c:$sortByCardHolderName"
    }

    fun hasChanges(): Boolean {
        return this.toString() != previousRefinements
    }
}

