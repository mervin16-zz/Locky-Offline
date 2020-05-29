package com.th3pl4gu3.locky_offline.repository.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.core.TestUtil
import com.th3pl4gu3.locky_offline.core.getValue
import com.th3pl4gu3.locky_offline.core.main.Card
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CardRepositoryTest {
    private lateinit var cardDao: CardDao
    private lateinit var database: Database
    private lateinit var cardsForUser1: List<Card>
    private lateinit var cardsForUser2: List<Card>
    private val user1 = "www.user1.com"
    private val user2 = "www.user2.com"

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, Database::class.java
        ).build()
        cardDao = database.cardDao()

        /* Add cards to the database beforehand */
        cardsForUser1 = TestUtil.createCards(10, user1)
        cardsForUser2 = TestUtil.createCards(15, user2)
        cardDao.insertAll(cardsForUser1)
        cardDao.insertAll(cardsForUser2)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun getAll() {
        //Arrange
        val expectedSize = 15

        //Act
        val result = getValue(cardDao.getAll(user2)).size

        //Assert
        ViewMatchers.assertThat(result, CoreMatchers.equalTo(expectedSize))
    }

    @Test
    @Throws(Exception::class)
    fun get() = runBlocking {
        //Arrange
        val card = cardsForUser1[5]
        val expectedName = card.entryName

        //Act
        val result = cardDao.get(card.cardID)?.entryName

        //Assert
        ViewMatchers.assertThat(result, CoreMatchers.equalTo(expectedName))
    }

    @Test
    @Throws(Exception::class)
    fun remove() = runBlocking {
        //Arrange
        val card = cardsForUser2[5]
        val expectedSize = 14

        //Act
        cardDao.remove(card.cardID)
        val size = getValue(cardDao.getAll(user2)).size
        val fetchedCard = cardDao.get(card.cardID)

        //Assert
        ViewMatchers.assertThat(size, CoreMatchers.equalTo(expectedSize))
        Assert.assertNull(fetchedCard)
    }

    @Test
    @Throws(Exception::class)
    fun removeAll() = runBlocking {
        //Arrange
        val card = cardsForUser1[5]
        val expectedSize1 = 0
        val expectedSize2 = 15

        //Act
        cardDao.removeAll(user1)
        val size1 = getValue(cardDao.getAll(user1)).size
        val size2 = getValue(cardDao.getAll(user2)).size
        val fetchedCard = cardDao.get(card.cardID)

        //Assert
        ViewMatchers.assertThat(size1, CoreMatchers.equalTo(expectedSize1))
        ViewMatchers.assertThat(size2, CoreMatchers.equalTo(expectedSize2))
        Assert.assertNull(fetchedCard)
    }

    @Test
    @Throws(Exception::class)
    fun insert() = runBlocking {
        //Arrange
        val card = TestUtil.getCard(20, user2)
        val expectedSize = 16
        val expectedName = card.entryName

        //Act
        cardDao.insert(card)
        val size = getValue(cardDao.getAll(user2)).size
        val fetchedCard = cardDao.get(card.cardID)

        //Assert
        ViewMatchers.assertThat(size, CoreMatchers.equalTo(expectedSize))
        ViewMatchers.assertThat(fetchedCard?.entryName, CoreMatchers.equalTo(expectedName))
    }

    @Test
    @Throws(Exception::class)
    fun update() = runBlocking {
        //Arrange
        val card = cardsForUser1[6]
        val newName = "Accounting"
        card.entryName = newName

        //Act
        cardDao.update(card)
        val fetchedCard = cardDao.get(card.cardID)

        //Assert
        ViewMatchers.assertThat(fetchedCard?.entryName, CoreMatchers.equalTo(newName))
    }
}