package com.th3pl4gu3.locky_offline.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.core.main.User

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Account::class, Card::class, User::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun cardDao(): CardDao
    abstract fun userDao(): UserDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: com.th3pl4gu3.locky_offline.repository.database.Database? = null

        fun getDatabase(context: Context): com.th3pl4gu3.locky_offline.repository.database.Database {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.th3pl4gu3.locky_offline.repository.database.Database::class.java,
                    "locky_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}