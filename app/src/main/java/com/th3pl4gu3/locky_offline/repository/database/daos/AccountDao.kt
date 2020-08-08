package com.th3pl4gu3.locky_offline.repository.database.daos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.th3pl4gu3.locky_offline.core.credentials.Account

/*
* CRUD for the Account object
*/
@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<Account>)

    @Update
    suspend fun update(account: Account)

    @Query("DELETE FROM account_table WHERE id = :key")
    suspend fun remove(key: Int)

    @Query("DELETE FROM account_table WHERE userID = :userID")
    suspend fun removeAll(userID: String)

    @Query("SELECT * FROM account_table WHERE id = :key")
    suspend fun get(key: Int): Account?

    @Query("SELECT * FROM account_table WHERE userID = :userID")
    fun getAll(userID: String): DataSource.Factory<Int, Account>

    @Query("SELECT * FROM account_table WHERE userID = :userID AND (entryName LIKE :query OR username LIKE :query OR website LIKE :query)")
    fun search(query: String, userID: String): LiveData<List<Account>>

    @Query("SELECT COUNT(entryName) FROM account_table WHERE userID = :userID")
    fun size(userID: String): LiveData<Int>
}