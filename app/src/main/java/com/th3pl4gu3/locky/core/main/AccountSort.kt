package com.th3pl4gu3.locky.core.main

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class AccountSort(
    var name: Boolean = false,
    var username: Boolean = false,
    var email: Boolean = false,
    var website: Boolean = false,
    var authType: Boolean = false
) : Parcelable {

    @IgnoredOnParcel
    private val previousRefinements: String

    init {
        previousRefinements = this.toString()
    }

    override fun toString(): String {
        return "nm:$name|un:$username|em:$email|ws:$website|at:$authType"
    }

    fun hasChanges(): Boolean {
        return this.toString() != previousRefinements
    }
}

