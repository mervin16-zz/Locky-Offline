package com.th3pl4gu3.locky_offline.repository.database

import android.app.Application
import com.th3pl4gu3.locky_offline.core.main.Account

class AccountRepository private constructor(application: Application) {

    private val database = Database.getDatabase(application)
    private val accountDao = database.accountDao()

    companion object {

        @Volatile
        private var instance: AccountRepository? = null

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: AccountRepository(application).also { instance = it }
            }
    }

    fun getAll(userID: String) = accountDao.getAll(userID)

    suspend fun get(key: String) = accountDao.get(key)

    suspend fun insert(account: Account) = accountDao.insert(account)

    suspend fun update(account: Account) = accountDao.update(account)

    suspend fun delete(key: String) = accountDao.remove(key)

    suspend fun wipe(userID: String) = accountDao.removeAll(userID)
}