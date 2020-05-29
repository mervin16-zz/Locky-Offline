package com.th3pl4gu3.locky_offline.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.th3pl4gu3.locky_offline.core.main.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("DELETE FROM user_table WHERE userID = :key")
    suspend fun remove(key: String)

    @Query("SELECT * FROM user_table WHERE userID = :key")
    fun get(key: String): LiveData<User>
}