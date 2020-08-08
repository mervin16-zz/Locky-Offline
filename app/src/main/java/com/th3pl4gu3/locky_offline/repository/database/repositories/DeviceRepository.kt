package com.th3pl4gu3.locky_offline.repository.database.repositories

import android.app.Application
import com.th3pl4gu3.locky_offline.core.credentials.Device
import com.th3pl4gu3.locky_offline.repository.database.LockyDatabase
import java.util.*

/*
* Repository pattern for device CRUD
*/
class DeviceRepository private constructor(application: Application) {

    private val database =
        LockyDatabase.getDatabase(
            application
        )
    private val deviceDao = database.deviceDao()

    companion object {
        @Volatile
        private var instance: DeviceRepository? = null

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: DeviceRepository(application).also { instance = it }
            }
    }

    suspend fun get(key: Int) = deviceDao.get(key)

    suspend fun insert(device: Device) = deviceDao.insert(device)

    suspend fun update(device: Device) = deviceDao.update(device)

    suspend fun delete(key: Int) = deviceDao.remove(key)

    suspend fun wipe(userID: String) = deviceDao.removeAll(userID)

    fun size(userID: String) = deviceDao.size(userID)

    fun getAll(userID: String) = deviceDao.getAll(userID)

    fun search(query: String, userID: String) =
        deviceDao.search(query.toLowerCase(Locale.ROOT), userID)
}