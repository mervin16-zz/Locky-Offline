package com.th3pl4gu3.locky_offline.repository.database.daos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.th3pl4gu3.locky_offline.core.credentials.BankAccount

/*
* CRUD for the Bank Account object
*/
@Dao
interface BankAccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bankAccount: BankAccount)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bankAccounts: List<BankAccount>)

    @Update
    suspend fun update(bankAccount: BankAccount)

    @Query("DELETE FROM bank_account_table WHERE id = :key")
    suspend fun remove(key: Int)

    @Query("DELETE FROM bank_account_table WHERE userID = :userID")
    suspend fun removeAll(userID: String)

    @Query("SELECT * FROM bank_account_table WHERE id = :key")
    suspend fun get(key: Int): BankAccount?

    @Query("SELECT * FROM bank_account_table WHERE userID = :userID")
    fun getAll(userID: String): DataSource.Factory<Int, BankAccount>

    @Query("SELECT * FROM bank_account_table WHERE userID = :userID AND (entryName LIKE :query OR accountOwner LIKE :query OR bank LIKE :query)")
    fun search(query: String, userID: String): LiveData<List<BankAccount>>

    @Query("SELECT COUNT(entryName) FROM bank_account_table WHERE userID = :userID")
    fun size(userID: String): LiveData<Int>
}