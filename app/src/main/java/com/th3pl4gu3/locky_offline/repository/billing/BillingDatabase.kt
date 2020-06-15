package com.th3pl4gu3.locky_offline.repository.billing

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
* The database that concerns billing only
* Extends the parent class RoomDatabase() of Android
* Entities defined here are for the billing objects that need to be stored in this Database
* should ONLY define entities regarding billing services.
*/
@Database(
    entities = [AugmentedSkuDetails::class],
    version = 1,
    exportSchema = false
)
abstract class BillingDatabase : RoomDatabase() {

    /*
    * Objects Dao defined here
    */
    abstract fun donationDao(): DonationDao

    companion object {
        /*
        * Singleton prevents multiple instances of database opening at the
        * same time.
        */
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