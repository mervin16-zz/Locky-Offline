package com.th3pl4gu3.locky_offline.core.main

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.th3pl4gu3.locky_offline.ui.main.utils.toFormattedStringDefault
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "user_table")
data class User constructor(
    @Ignore var name: String,
    @Ignore var photo: String,
    @PrimaryKey @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "dateJoined") var dateJoined: String = Calendar.getInstance()
        .toFormattedStringDefault()
) : Parcelable {

    constructor(email: String, dateJoined: String) : this(
        "",
        "",
        email = email,
        dateJoined = dateJoined
    )

    companion object {
        private var instance: User? = null

        fun getInstance(account: GoogleSignInAccount): User {
            return if (instance != null) {
                instance?.apply {
                    name = account.displayName!!
                    email = account.email!!
                    photo = account.photoUrl.toString()
                }!!
            } else {
                instance = User(
                    name = account.displayName!!,
                    email = account.email!!,
                    photo = account.photoUrl.toString()
                )
                instance!!
            }
        }
    }

}

