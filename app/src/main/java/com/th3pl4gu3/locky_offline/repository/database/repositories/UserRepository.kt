package com.th3pl4gu3.locky_offline.repository.database.repositories

import android.app.Application
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.repository.database.LockyDatabase

class UserRepository private constructor(application: Application) {

    private val database =
        LockyDatabase.getDatabase(
            application
        )
    private val userDao = database.userDao()

    companion object {

        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(application: Application) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: UserRepository(
                            application
                        )
                            .also { instance = it }
                }
    }

    suspend fun get(key: String) = userDao.get(key)

    suspend fun insert(user: User) = userDao.insert(user)

    suspend fun delete(key: String) = userDao.remove(key)
}