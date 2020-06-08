package com.th3pl4gu3.locky_offline.repository.billing

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AugmentedSkuDetails::class],
    version = 1,
    exportSchema = false
)
abstract class BillingDatabase : RoomDatabase() {

    abstract fun donationDao(): DonationDao

    companion object {
        @Volatile
        private var INSTANCE: BillingDatabase? = null

        fun getInstance(context: Context): BillingDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                BillingDatabase::class.java,
                "billing_db"
            )
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}