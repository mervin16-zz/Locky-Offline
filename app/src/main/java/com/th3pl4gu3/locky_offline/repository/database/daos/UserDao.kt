package com.th3pl4gu3.locky_offline.repository.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.th3pl4gu3.locky_offline.core.others.User

/*
* CRUD for the User object
*/
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table WHERE email = :key")
    suspend fun remove(key: String)

    @Query("SELECT * FROM user_table WHERE email = :key")
    suspend fun get(key: String): User?
}