package com.th3pl4gu3.locky_offline.repository.database.daos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.th3pl4gu3.locky_offline.core.credentials.Card

/*
* CRUD for the Card object
*/
@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<Card>)

    @Update
    suspend fun update(card: Card)

    @Query("DELETE FROM card_table WHERE id = :key")
    suspend fun remove(key: Int)

    @Query("DELETE FROM card_table WHERE userID = :userID")
    suspend fun removeAll(userID: String)

    @Query("SELECT * FROM card_table WHERE id = :key")
    suspend fun get(key: Int): Card?

    @Query("SELECT * FROM card_table WHERE userID = :userID")
    fun getAll(userID: String): DataSource.Factory<Int, Card>

    @Query("SELECT * FROM card_table WHERE userID = :userID AND (entryName LIKE :query OR number LIKE :query OR bank LIKE :query)")
    fun search(query: String, userID: String): LiveData<List<Card>>

    @Query("SELECT COUNT(entryName) FROM card_table WHERE userID = :userID")
    fun size(userID: String): LiveData<Int>
}