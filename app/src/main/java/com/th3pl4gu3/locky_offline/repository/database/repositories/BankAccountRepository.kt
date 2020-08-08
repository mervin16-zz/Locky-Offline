package com.th3pl4gu3.locky_offline.repository.database.repositories

import android.app.Application
import com.th3pl4gu3.locky_offline.core.credentials.BankAccount
import com.th3pl4gu3.locky_offline.repository.database.LockyDatabase
import java.util.*

/*
* Repository pattern for bank account CRUD
*/
class BankAccountRepository private constructor(application: Application) {

    private val database =
        LockyDatabase.getDatabase(
            application
        )
    private val bankAccountDao = database.bankAccountDao()

    companion object {
        @Volatile
        private var instance: BankAccountRepository? = null

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: BankAccountRepository(application).also { instance = it }
            }
    }

    suspend fun get(key: Int) = bankAccountDao.get(key)

    suspend fun insert(bankAccount: BankAccount) = bankAccountDao.insert(bankAccount)

    suspend fun update(bankAccount: BankAccount) = bankAccountDao.update(bankAccount)

    suspend fun delete(key: Int) = bankAccountDao.remove(key)

    suspend fun wipe(userID: String) = bankAccountDao.removeAll(userID)

    fun size(userID: String) = bankAccountDao.size(userID)

    fun getAll(userID: String) = bankAccountDao.getAll(userID)

    fun search(query: String, userID: String) =
        bankAccountDao.search(query.toLowerCase(Locale.ROOT), userID)
}