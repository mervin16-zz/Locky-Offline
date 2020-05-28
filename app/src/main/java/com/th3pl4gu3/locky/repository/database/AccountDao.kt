package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.th3pl4gu3.locky.core.main.Account

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<Account>)

    @Update
    suspend fun update(account: Account)

    @Query("DELETE FROM account_table WHERE accountID = :key")
    suspend fun remove(key: String)

    @Query("DELETE FROM account_table")
    suspend fun removeAll()

    @Query("SELECT * FROM account_table WHERE accountID = :key")
    fun get(key: String): LiveData<Account>

    @Query("SELECT * FROM account_table")
    fun getAll(): LiveData<List<Account>>
}