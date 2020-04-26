package com.th3pl4gu3.locky.core.tuning

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class AccountSort(
    var website: Boolean = false,
    var email: Boolean = false,
    var twofa: Boolean = false
) : Parcelable {

    @IgnoredOnParcel
    private val previousRefinements: String

    init {
        previousRefinements = this.toString()
    }

    override fun toString(): String {
        return "w:$website|e:$email|t:$twofa"
    }

    fun hasChanges(): Boolean {
        return this.toString() != previousRefinements
    }
}

