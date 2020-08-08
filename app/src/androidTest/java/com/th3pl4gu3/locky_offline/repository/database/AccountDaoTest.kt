package com.th3pl4gu3.locky_offline.repository.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.TestUtil
import com.th3pl4gu3.locky_offline.core.credentials.Account
import com.th3pl4gu3.locky_offline.core.getValue
import com.th3pl4gu3.locky_offline.repository.database.daos.AccountDao
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AccountDaoTest {
    private lateinit var accountDao: AccountDao
    private lateinit var database: LockyDatabase
    private lateinit var accountsForUser1: List<Account>
    private lateinit var accountsForUser2: List<Account>
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
        accountDao = database.accountDao()

        /*
        * We have created two users, user1 & user 2
        * We add accounts for each user separately
        * We then perform the tests
        */
        accountsForUser1 = TestUtil.createAccounts(15, user1)
        accountsForUser2 = TestUtil.createAccounts(10, user2)
        accountDao.insertAll(accountsForUser1)
        accountDao.insertAll(accountsForUser2)
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
        val accountsForUser1 = getValue(accountDao.getAll(user1)) //Get all accounts for user1
        val accountsForUser2 = getValue(accountDao.getAll(user2)) //Get all accounts for user2
        val resultSizeForUser1 = accountsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = accountsForUser2.size //Fetch all for user 2

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
        accountDao.removeAll(user1)
        accountDao.removeAll(user2)
        /* Try to get now */
        val accountsForUser1 = getValue(accountDao.getAll(user1)) //Get all accounts for user1
        val accountsForUser2 = getValue(accountDao.getAll(user2)) //Get all accounts for user2
        val resultSizeForUser1 = accountsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = accountsForUser2.size //Fetch all for user 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun getOne_DataExists() = runBlocking {
        /* Arrange */
        var accountForUser1: Account? = null
        var accountForUser2: Account? = null
        var resultForUser1: Account? = null
        var resultForUser2: Account? = null
        /* Act */
        val accountsForUser1 = getValue(accountDao.getAll(user1)) //Get all accounts for user1
        val accountsForUser2 = getValue(accountDao.getAll(user2)) //Get all accounts for user2
        accountForUser1 = accountsForUser1.last() //Fetch last for user 1
        accountForUser2 = accountsForUser2.last() //Fetch last for user 2
        /* Test single get */
        resultForUser1 = accountDao.get(accountForUser1.id)
        resultForUser2 = accountDao.get(accountForUser2.id)

        /* Assert */
        Assert.assertNotNull(resultForUser1)
        Assert.assertNotNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun getOne_DataDoesNotExists() = runBlocking {
        /* Arrange */
        var resultForUser1: Account? = null
        var resultForUser2: Account? = null

        /* Act */
        resultForUser1 = accountDao.get(9846)
        resultForUser2 = accountDao.get(5132)

        /* Assert */
        Assert.assertNull(resultForUser1)
        Assert.assertNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun removeOne_DataExists() = runBlocking {
        /* Arrange */
        var accountForUser1: Account? = null
        var accountForUser2: Account? = null
        var resultForUser1: Account? = null
        var resultForUser2: Account? = null

        /* Act */
        val accountsForUser1 = getValue(accountDao.getAll(user1)) //Get all accounts for user1
        val accountsForUser2 = getValue(accountDao.getAll(user2)) //Get all accounts for user2
        accountForUser1 = accountsForUser1.last() //Fetch last for user 1
        accountForUser2 = accountsForUser2.last() //Fetch last for user 2
        /* Test remove one */
        accountDao.remove(accountForUser1.id)
        accountDao.remove(accountForUser2.id)
        /* Try to fetch data now */
        resultForUser1 = accountDao.get(accountForUser1.id)
        resultForUser2 = accountDao.get(accountForUser2.id)

        /* Assert */
        Assert.assertNull(resultForUser1)
        Assert.assertNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun removeOne_DataDoesNotExists() = runBlocking {
        /* Arrange */
        var resultForUser1: Account? = null
        var resultForUser2: Account? = null

        /* Act */
        /* Remove object that doesn't exist */
        accountDao.remove(65)
        accountDao.remove(85)
        /* Try to fetch data now */
        resultForUser1 = accountDao.get(65)
        resultForUser2 = accountDao.get(65)

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
        accountDao.removeAll(user1)
        accountDao.removeAll(user2)
        /* Try to get now */
        val accountsForUser1 = getValue(accountDao.getAll(user1)) //Get all accounts for user1
        val accountsForUser2 = getValue(accountDao.getAll(user2)) //Get all accounts for user2
        val resultSizeForUser1 = accountsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = accountsForUser2.size //Fetch all for user 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun update_DataExists() = runBlocking {
        /* Arrange */
        var accountForUser1: Account? = null
        var accountForUser2: Account? = null
        val expectedNewEntryNameForUser1 = "EntryUser1"
        val expectedNewEntryNameForUser2 = "EntryUser2"

        /* Act */
        val accountsForUser1 = getValue(accountDao.getAll(user1)) //Get all accounts for user1
        val accountsForUser2 = getValue(accountDao.getAll(user2)) //Get all accounts for user2
        accountForUser1 = accountsForUser1.last() //Fetch all for user 1
        accountForUser2 = accountsForUser2.last() //Fetch all for user 2
        accountForUser1.entryName = expectedNewEntryNameForUser1
        accountForUser2.entryName = expectedNewEntryNameForUser2
        /* Test update */
        accountDao.update(accountForUser1)
        accountDao.update(accountForUser2)

        /* Assert */
        assertThat(
            accountDao.get(accountForUser1.id)?.entryName,
            equalTo(expectedNewEntryNameForUser1)
        )
        assertThat(
            accountDao.get(accountForUser2.id)?.entryName,
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
        accountDao.update(
            Account()
                .apply { this.id = 89 })
        /* Try to get now */
        val accountsForUser1 = getValue(accountDao.getAll(user1)) //Get all accounts for user1
        val accountsForUser2 = getValue(accountDao.getAll(user2)) //Get all accounts for user2
        val resultSizeForUser1 = accountsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = accountsForUser2.size //Fetch all for user 2

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
        val resultSizeForUser1 = getValue(accountDao.size(user1)) //Get all size for user1
        val resultSizeForUser2 = getValue(accountDao.size(user2)) //Get all size for user2 2

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
        accountDao.removeAll(user1)
        accountDao.removeAll(user2)
        val resultSizeForUser1 = getValue(accountDao.size(user1)) //Get all size for user1
        val resultSizeForUser2 = getValue(accountDao.size(user2)) //Get all size for user2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun search_DataExists_1Of2() = runBlocking {
        /* Arrange */
        val query = "%count%"
        val expectedSizeForUser1 = 15 //Size for user1 only
        val expectedSizeForUser2 = 10 //Size for user2 only

        /* Act */
        val resultSizeForUser1 =
            getValue(accountDao.search(query, user1)).size //Get all size for user1
        val resultSizeForUser2 =
            getValue(accountDao.search(query, user2)).size //Get all size for user2

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
            getValue(accountDao.search(query, user1)).size //Get all size for user1
        val resultSizeForUser2 =
            getValue(accountDao.search(query, user2)).size //Get all size for user2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

}