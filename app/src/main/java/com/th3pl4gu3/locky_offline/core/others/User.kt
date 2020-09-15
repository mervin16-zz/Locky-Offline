package com.th3pl4gu3.locky_offline.core.others

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toFormattedStringDefault
import kotlinx.android.parcel.Parcelize
import java.util.*

/*
* User's data is stored in this class
* User's data is fetched from google
* Name & photo should NOT be stored in local db
*/
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

        fun getInstance(displayName: String, email: String, photoUrl: String): User {
            return if (instance != null) {
                instance?.apply {
                    this.name = displayName
                    this.email = email
                    this.photo = photoUrl
                }!!
            } else {
                instance =
                    User(
                        name = displayName,
                        email = email,
                        photo = photoUrl
                    )
                instance!!
            }
        }
    }

}

