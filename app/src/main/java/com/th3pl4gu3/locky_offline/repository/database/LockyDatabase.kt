package com.th3pl4gu3.locky_offline.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.BankAccount
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.repository.database.daos.AccountDao
import com.th3pl4gu3.locky_offline.repository.database.daos.BankAccountDao
import com.th3pl4gu3.locky_offline.repository.database.daos.CardDao
import com.th3pl4gu3.locky_offline.repository.database.daos.UserDao

/*
* The main Locky database that stores all locky objects
* Extends the parent class RoomDatabase() of Android
* Entities defined here are for the Locky main objects that need to be stored in this Database
* should ONLY define entities extending the Credentials class.
* The user object entity also should be defined here.
*/
@Database(
    entities = [Account::class, Card::class, BankAccount::class, User::class],
    version = 1,
    exportSchema = false
)
abstract class LockyDatabase : RoomDatabase() {

    /*
    * Objects Dao defined here
    */
    abstract fun accountDao(): AccountDao
    abstract fun cardDao(): CardDao
    abstract fun userDao(): UserDao
    abstract fun bankAccountDao(): BankAccountDao

    companion object {
        /*
        * Singleton prevents multiple instances of database opening at the
        * same time.
        */
        @Volatile
        private var INSTANCE: LockyDatabase? = null

        fun getDatabase(context: Context): LockyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LockyDatabase::class.java,
                    "locky_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}