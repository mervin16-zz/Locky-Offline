package com.th3pl4gu3.locky.repository.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky.core.TestUtil
import com.th3pl4gu3.locky.core.getValue
import com.th3pl4gu3.locky.core.main.Card
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CardRepositoryTest {
    private lateinit var cardDao: CardDao
    private lateinit var database: Database
    private lateinit var cards: List<Card>
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
        cards = TestUtil.createCards(10)
        cardDao.insertAll(cards)
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
        val expectedSize = 10

        //Act
        val result = getValue(cardDao.getAll()).size

        //Assert
        ViewMatchers.assertThat(expectedSize, CoreMatchers.equalTo(result))
    }

    @Test
    @Throws(Exception::class)
    fun get() {
        //Arrange
        val card = cards[5]
        val expectedName = card.entryName

        //Act
        val result = getValue(cardDao.get(card.cardID)).entryName

        //Assert
        ViewMatchers.assertThat(expectedName, CoreMatchers.equalTo(result))
    }

    @Test
    @Throws(Exception::class)
    fun remove() = runBlocking {
        //Arrange
        val card = cards[5]
        val expectedSize = 9

        //Act
        cardDao.remove(card.cardID)
        val size = getValue(cardDao.getAll()).size
        val fetchedCard = getValue(cardDao.get(card.cardID))

        //Assert
        ViewMatchers.assertThat(expectedSize, CoreMatchers.equalTo(size))
        Assert.assertNull(fetchedCard)
    }

    @Test
    @Throws(Exception::class)
    fun removeAll() = runBlocking {
        //Arrange
        val card = cards[5]
        val expectedSize = 0
        val expectedName = card.entryName

        //Act
        cardDao.removeAll()
        val size = getValue(cardDao.getAll()).size
        val fetchedCard = getValue(cardDao.get(card.cardID))

        //Assert
        ViewMatchers.assertThat(expectedSize, CoreMatchers.equalTo(size))
        Assert.assertNull(fetchedCard)
    }

    @Test
    @Throws(Exception::class)
    fun insert() = runBlocking {
        //Arrange
        val card = TestUtil.getCard(20)
        val expectedSize = 11
        val expectedName = card.entryName

        //Act
        cardDao.insert(card)
        val size = getValue(cardDao.getAll()).size
        val fetchedCard = getValue(cardDao.get(card.cardID))

        //Assert
        ViewMatchers.assertThat(expectedSize, CoreMatchers.equalTo(size))
        ViewMatchers.assertThat(expectedName, CoreMatchers.equalTo(fetchedCard.entryName))
    }

    @Test
    @Throws(Exception::class)
    fun update() = runBlocking {
        //Arrange
        val card = cards[6]
        val newName = "Accounting"
        card.entryName = newName

        //Act
        cardDao.update(card)
        val fetchedCard = getValue(cardDao.get(card.cardID))

        //Assert
        ViewMatchers.assertThat(newName, CoreMatchers.equalTo(fetchedCard.entryName))
    }
}