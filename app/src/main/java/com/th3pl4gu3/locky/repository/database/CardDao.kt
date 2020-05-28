package com.th3pl4gu3.locky.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.th3pl4gu3.locky.core.main.Card

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<Card>)

    @Update
    suspend fun update(card: Card)

    @Query("DELETE FROM card_table WHERE cardID = :key")
    suspend fun remove(key: String)

    @Query("DELETE FROM card_table")
    suspend fun removeAll()

    @Query("SELECT * FROM card_table WHERE cardID = :key")
    fun get(key: String): LiveData<Card>

    @Query("SELECT * FROM card_table")
    fun getAll(): LiveData<List<Card>>
}