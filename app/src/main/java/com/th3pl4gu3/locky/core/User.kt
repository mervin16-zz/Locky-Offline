package com.th3pl4gu3.locky.core

import android.os.Parcelable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Exclude
import com.th3pl4gu3.locky.ui.main.utils.toFormattedStringDefault
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(
    var id: String = "",
    private val _name: String = "",
    private val _photoUrl: String = "",
    var email: String = "",
    var dateJoined: String = Calendar.getInstance().toFormattedStringDefault(),
    var accountType: AccountType = AccountType.TRIAL
) : Parcelable {

    enum class AccountType { TRIAL, NORMAL, SUPER }

    val name: String
        @Exclude get() = _name

    val photoUrl: String?
        @Exclude get() = _photoUrl

    companion object {
        private const val TAG = "USER_CLASS_TEST"
        fun getInstance() = getUser()

        private fun getUser(): User {
            val user = FirebaseAuth.getInstance().currentUser

            return User(
                _name = user?.displayName!!,
                _photoUrl = user.photoUrl.toString(),
                email = user.email!!
            )
        }
    }
}

