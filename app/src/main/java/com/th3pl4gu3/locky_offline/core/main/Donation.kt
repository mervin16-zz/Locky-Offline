package com.th3pl4gu3.locky_offline.core.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

abstract class Donation(
    @PrimaryKey var id: Int = 1
)

@Entity(tableName = "donation_cookie")
data class Cookie(
    @ColumnInfo(name = "hasPurchased")
    var hasPurchased: Boolean
) : Donation()

@Entity(tableName = "donation_sandwich")
data class Sandwich(
    @ColumnInfo(name = "hasPurchased")
    var hasPurchased: Boolean
) : Donation()

@Entity(tableName = "donation_milkshake")
data class Milkshake(
    @ColumnInfo(name = "hasPurchased")
    var hasPurchased: Boolean
) : Donation()

@Entity(tableName = "donation_burger")
data class Burger(
    @ColumnInfo(name = "hasPurchased")
    var hasPurchased: Boolean
) : Donation()

@Entity(tableName = "donation_gift")
data class Gift(
    @ColumnInfo(name = "hasPurchased")
    var hasPurchased: Boolean
) : Donation()

@Entity(tableName = "donation_star")
data class Star(
    @ColumnInfo(name = "hasPurchased")
    var hasPurchased: Boolean
) : Donation()