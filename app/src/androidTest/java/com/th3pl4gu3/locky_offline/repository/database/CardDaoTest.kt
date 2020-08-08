package com.th3pl4gu3.locky_offline.repository.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.TestUtil
import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.core.getValue
import com.th3pl4gu3.locky_offline.repository.database.daos.CardDao
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CardDaoTest {
    private lateinit var cardDao: CardDao
    private lateinit var database: LockyDatabase
    private lateinit var cardsForUser1: List<Card>
    private lateinit var cardsForUser2: List<Card>
    private val user1 = "user1@email.com"
    private val user2 = "user2@email.com"

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, LockyDatabase::class.java
        ).build()
        cardDao = database.cardDao()

        /*
        * We have created two users, user1 & user 2
        * We add cards for each user separately
        * We then perform the tests
        */
        cardsForUser1 = TestUtil.createCards(15, user1)
        cardsForUser2 = TestUtil.createCards(10, user2)
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
    fun getAll_DataExists() {
        /* Arrange */
        val expectedSizeForUser1 = 15 //Size for user1 only
        val expectedSizeForUser2 = 10 //Size for user2 only

        /* Act */
        val cardsForUser1 = getValue(cardDao.getAll(user1)) //Get all cards for user1
        val cardsForUser2 = getValue(cardDao.getAll(user2)) //Get all cards for user2
        val resultSizeForUser1 = cardsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = cardsForUser2.size //Fetch all for user 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun getAll_DataDoesNotExists() = runBlocking {
        /* Arrange */
        val expectedSizeForUser1 = 0 //Size for user1 only
        val expectedSizeForUser2 = 0 //Size for user2 only

        /* Act */
        /* Wipe data first */
        cardDao.removeAll(user1)
        cardDao.removeAll(user2)
        /* Try to get now */
        val cardsForUser1 = getValue(cardDao.getAll(user1)) //Get all cards for user1
        val cardsForUser2 = getValue(cardDao.getAll(user2)) //Get all cards for user2
        val resultSizeForUser1 = cardsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = cardsForUser2.size //Fetch all for user 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun getOne_DataExists() = runBlocking {
        /* Arrange */
        var cardForUser1: Card? = null
        var cardForUser2: Card? = null
        var resultForUser1: Card? = null
        var resultForUser2: Card? = null
        /* Act */
        val cardsForUser1 = getValue(cardDao.getAll(user1)) //Get all cards for user1
        val cardsForUser2 = getValue(cardDao.getAll(user2)) //Get all cards for user2
        cardForUser1 = cardsForUser1.last() //Fetch last for user 1
        cardForUser2 = cardsForUser2.last() //Fetch last for user 2
        /* Test single get */
        resultForUser1 = cardDao.get(cardForUser1.id)
        resultForUser2 = cardDao.get(cardForUser2.id)

        /* Assert */
        Assert.assertNotNull(resultForUser1)
        Assert.assertNotNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun getOne_DataDoesNotExists() = runBlocking {
        /* Arrange */
        var resultForUser1: Card? = null
        var resultForUser2: Card? = null

        /* Act */
        resultForUser1 = cardDao.get(9846)
        resultForUser2 = cardDao.get(5132)

        /* Assert */
        Assert.assertNull(resultForUser1)
        Assert.assertNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun removeOne_DataExists() = runBlocking {
        /* Arrange */
        var cardForUser1: Card? = null
        var cardForUser2: Card? = null
        var resultForUser1: Card? = null
        var resultForUser2: Card? = null

        /* Act */
        val cardsForUser1 = getValue(cardDao.getAll(user1)) //Get all card for user1
        val cardsForUser2 = getValue(cardDao.getAll(user2)) //Get all card for user2
        cardForUser1 = cardsForUser1.last() //Fetch last for user 1
        cardForUser2 = cardsForUser2.last() //Fetch last for user 2
        /* Test remove one */
        cardDao.remove(cardForUser1.id)
        cardDao.remove(cardForUser2.id)
        /* Try to fetch data now */
        resultForUser1 = cardDao.get(cardForUser1.id)
        resultForUser2 = cardDao.get(cardForUser2.id)

        /* Assert */
        Assert.assertNull(resultForUser1)
        Assert.assertNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun removeOne_DataDoesNotExists() = runBlocking {
        /* Arrange */
        var resultForUser1: Card? = null
        var resultForUser2: Card? = null

        /* Act */
        /* Remove object that doesn't exist */
        cardDao.remove(65)
        cardDao.remove(85)
        /* Try to fetch data now */
        resultForUser1 = cardDao.get(65)
        resultForUser2 = cardDao.get(65)

        /* Assert */
        Assert.assertNull(resultForUser1)
        Assert.assertNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun removeAll() = runBlocking {
        /* Arrange */
        val expectedSizeForUser1 = 0 //Size for user1 only
        val expectedSizeForUser2 = 0 //Size for user2 only

        /* Act */
        /* Wipe data first */
        cardDao.removeAll(user1)
        cardDao.removeAll(user2)
        /* Try to get now */
        val cardsForUser1 = getValue(cardDao.getAll(user1)) //Get all cards for user1
        val cardsForUser2 = getValue(cardDao.getAll(user2)) //Get all cards for user2
        val resultSizeForUser1 = cardsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = cardsForUser2.size //Fetch all for user 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun update_DataExists() = runBlocking {
        /* Arrange */
        var cardForUser1: Card? = null
        var cardForUser2: Card? = null
        val expectedNewEntryNameForUser1 = "EntryUser1"
        val expectedNewEntryNameForUser2 = "EntryUser2"

        /* Act */
        val cardsForUser1 = getValue(cardDao.getAll(user1)) //Get all cards for user1
        val cardsForUser2 = getValue(cardDao.getAll(user2)) //Get all cards for user2
        cardForUser1 = cardsForUser1.last() //Fetch all for user 1
        cardForUser2 = cardsForUser2.last() //Fetch all for user 2
        cardForUser1.entryName = expectedNewEntryNameForUser1
        cardForUser2.entryName = expectedNewEntryNameForUser2
        /* Test update */
        cardDao.update(cardForUser1)
        cardDao.update(cardForUser2)

        /* Assert */
        assertThat(
            cardDao.get(cardForUser1.id)?.entryName,
            equalTo(expectedNewEntryNameForUser1)
        )
        assertThat(
            cardDao.get(cardForUser2.id)?.entryName,
            equalTo(expectedNewEntryNameForUser2)
        )
    }

    @Test
    @Throws(Exception::class)
    fun update_DataDoesNotExists() = runBlocking {
        /* Arrange */
        val expectedSizeForUser1 = 15 //Size for user1 only
        val expectedSizeForUser2 = 10 //Size for user2 only

        /* Act */
        /* Update non existing */
        cardDao.update(
            Card()
                .apply { this.id = 89 })
        /* Try to get now */
        val cardsForUser1 = getValue(cardDao.getAll(user1)) //Get all cards for user1
        val cardsForUser2 = getValue(cardDao.getAll(user2)) //Get all cards for user2
        val resultSizeForUser1 = cardsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = cardsForUser2.size //Fetch all for user 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun size_DataExists() = runBlocking {
        /* Arrange */
        val expectedSizeForUser1 = 15 //Size for user1 only
        val expectedSizeForUser2 = 10 //Size for user2 only

        /* Act */
        val resultSizeForUser1 = getValue(cardDao.size(user1)) //Get all size for user1
        val resultSizeForUser2 = getValue(cardDao.size(user2)) //Get all size for user2 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun size_DataDoesNotExists() = runBlocking {
        /* Arrange */
        val expectedSizeForUser1 = 0 //Size for user1 only
        val expectedSizeForUser2 = 0 //Size for user2 only

        /* Act */
        //Wipe all data before testing
        cardDao.removeAll(user1)
        cardDao.removeAll(user2)
        val resultSizeForUser1 = getValue(cardDao.size(user1)) //Get all size for user1
        val resultSizeForUser2 = getValue(cardDao.size(user2)) //Get all size for user2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun search_DataExists_1Of2() = runBlocking {
        /* Arrange */
        val query = "%ard%"
        val expectedSizeForUser1 = 15 //Size for user1 only
        val expectedSizeForUser2 = 10 //Size for user2 only

        /* Act */
        val resultSizeForUser1 =
            getValue(cardDao.search(query, user1)).size //Get all size for user1
        val resultSizeForUser2 =
            getValue(cardDao.search(query, user2)).size //Get all size for user2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun search_DataExists_2Of2() = runBlocking {
        /* Arrange */
        val query = "%1%"
        val expectedSizeForUser1 = 7 //Size for user1 only
        val expectedSizeForUser2 = 2 //Size for user2 only

        /* Act */
        val resultSizeForUser1 =
            getValue(cardDao.search(query, user1)).size //Get all size for user1
        val resultSizeForUser2 =
            getValue(cardDao.search(query, user2)).size //Get all size for user2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

}