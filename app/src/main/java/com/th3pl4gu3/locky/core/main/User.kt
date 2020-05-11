package com.th3pl4gu3.locky.core.main

import android.os.Parcelable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Exclude
import com.th3pl4gu3.locky.ui.main.utils.toFormattedStringDefault
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User constructor(
    private val _id: String = "",
    private val _name: String = "",
    private val _photoUrl: String = "",
    var email: String = "",
    var dateJoined: String = Calendar.getInstance().toFormattedStringDefault(),
    var accountType: AccountType = AccountType.TRIAL,
    var accountStatus: AccountStatus = AccountStatus.ACTIVE
) : Parcelable {

    enum class AccountType { TRIAL, NORMAL, SUPER }
    enum class AccountStatus { ACTIVE, INACTIVE, BLOCKED }

    val id: String
        @Exclude get() = _id

    val name: String
        @Exclude get() = _name

    val photoUrl: String?
        @Exclude get() = _photoUrl

    companion object {
        private const val TAG = "USER_CLASS_TEST"
        fun getInstance() =
            getUser()

        private fun getUser(): User {
            val user = FirebaseAuth.getInstance().currentUser

            return User(
                _id = user?.uid!!,
                _name = user.displayName!!,
                _photoUrl = user.photoUrl.toString(),
                email = user.email!!
            )
        }
    }

    override fun toString(): String {
        return "ID: $id | name: $_name | photo: $_photoUrl | email: $email | Dj: $dateJoined | accountT: $accountType | accountS: $accountStatus"
    }
}

