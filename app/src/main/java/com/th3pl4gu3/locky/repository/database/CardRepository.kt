package com.th3pl4gu3.locky.repository.database

import android.app.Application
import com.th3pl4gu3.locky.core.main.Card

class CardRepository(application: Application) {

    private val database = Database.getDatabase(application)
    private val cardDao = database.cardDao()

    companion object {

        @Volatile
        private var instance: CardRepository? = null

        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: CardRepository(application).also { instance = it }
            }
    }

    val cards = cardDao.getAll()

    fun get(key: String) = cardDao.get(key)

    suspend fun insert(card: Card) = cardDao.insert(card)

    suspend fun update(card: Card) = cardDao.update(card)

    suspend fun delete(key: String) = cardDao.remove(key)

    suspend fun wipe() = cardDao.removeAll()
}