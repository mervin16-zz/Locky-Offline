package com.th3pl4gu3.locky_offline.repository.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


/*
* All migrations for Locky Room database
* Migrations is used when changes are done to the database
* The room needs to have a migration mechanism to preserve
* user data.
* Each time a new version of DB is defined, we need to migrate with a
* new function.
*/

object LockyMigration {
    internal val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `device_table` (`id` INTEGER NOT NULL, `entryName` TEXT NOT NULL, `userID` TEXT NOT NULL, `moreInfo` TEXT, `icon` TEXT NOT NULL, `username` TEXT NOT NULL, `password` TEXT NOT NULL, `accent` TEXT NOT NULL, `ipAddress` TEXT, `macAddress` TEXT, PRIMARY KEY(`id`))"
            )
        }
    }
}