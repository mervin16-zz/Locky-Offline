package com.th3pl4gu3.locky_offline.repository.database.daos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.th3pl4gu3.locky_offline.core.credentials.Device

/*
* CRUD for the Device object
*/
@Dao
interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: Device)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(Devices: List<Device>)

    @Update
    suspend fun update(device: Device)

    @Query("DELETE FROM device_table WHERE id = :key")
    suspend fun remove(key: Int)

    @Query("DELETE FROM device_table WHERE userID = :userID")
    suspend fun removeAll(userID: String)

    @Query("SELECT * FROM device_table WHERE id = :key")
    suspend fun get(key: Int): Device?

    @Query("SELECT * FROM device_table WHERE userID = :userID")
    fun getAll(userID: String): DataSource.Factory<Int, Device>

    @Query("SELECT * FROM device_table WHERE userID = :userID AND (entryName LIKE :query OR username LIKE :query)")
    fun search(query: String, userID: String): LiveData<List<Device>>

    @Query("SELECT COUNT(entryName) FROM device_table WHERE userID = :userID")
    fun size(userID: String): LiveData<Int>
}