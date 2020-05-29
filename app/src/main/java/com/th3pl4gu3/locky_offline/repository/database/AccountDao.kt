package com.th3pl4gu3.locky_offline.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.th3pl4gu3.locky_offline.core.main.Account

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

    @Query("DELETE FROM account_table WHERE userID = :userID")
    suspend fun removeAll(userID: String)

    @Query("SELECT * FROM account_table WHERE accountID = :key")
    suspend fun get(key: String): Account?

    @Query("SELECT * FROM account_table WHERE userID = :userID")
    fun getAll(userID: String): LiveData<List<Account>>
}