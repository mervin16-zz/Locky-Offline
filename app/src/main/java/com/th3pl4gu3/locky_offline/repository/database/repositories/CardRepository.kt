package com.th3pl4gu3.locky_offline.repository.database.repositories

import android.app.Application
import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.repository.database.LockyDatabase
import java.util.*

/*
* Repository pattern for card CRUD
*/
class CardRepository private constructor(application: Application) {

    private val database =
        LockyDatabase.getDatabase(
            application
        )
    private val cardDao = database.cardDao()

    companion object {
        @Volatile
        private var instance: CardRepository? = null

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CardRepository(application).also { instance = it }
            }
    }

    suspend fun get(key: Int) = cardDao.get(key)

    suspend fun insert(card: Card) = cardDao.insert(card)

    suspend fun update(card: Card) = cardDao.update(card)

    suspend fun delete(key: Int) = cardDao.remove(key)

    suspend fun wipe(userID: String) = cardDao.removeAll(userID)

    fun size(userID: String) = cardDao.size(userID)

    fun getAll(userID: String) = cardDao.getAll(userID)

    fun search(query: String, userID: String) =
        cardDao.search(query.toLowerCase(Locale.ROOT), userID)
}