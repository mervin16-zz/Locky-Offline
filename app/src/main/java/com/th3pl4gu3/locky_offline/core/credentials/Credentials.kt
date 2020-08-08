package com.th3pl4gu3.locky_offline.core.credentials

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

/*
* Parent class for the accounts, cards, etc...
* Any objects added that needs to store user credentials (passwords, pins, accounts)
* should extend this class.
*/
abstract class Credentials(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "entryName") var entryName: String = "",
    @ColumnInfo(name = "userID") var user: String = "",
    @ColumnInfo(name = "moreInfo") var additionalInfo: String? = null
)