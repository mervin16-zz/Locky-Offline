package com.th3pl4gu3.locky_offline.repository.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.core.TestUtil
import com.th3pl4gu3.locky_offline.core.getValue
import com.th3pl4gu3.locky_offline.core.main.Account
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AccountRepositoryTest {
    private lateinit var accountDao: AccountDao
    private lateinit var database: Database
    private lateinit var accountsForUser1: List<Account>
    private lateinit var accountsForUser2: List<Account>
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
    fun getAll() {
        //Arrange
        val expectedSize1 = 15
        val expectedSize2 = 0

        //Act
        val result1 = getValue(accountDao.getAll(user1)).size
        val result2 = getValue(accountDao.getAll("no match")).size

        //Assert
        assertThat(result1, equalTo(expectedSize1))
        assertThat(result2, equalTo(expectedSize2))
    }

    @Test
    @Throws(Exception::class)
    fun get() = runBlocking {
        //Arrange
        val account = accountsForUser1[5]
        val expectedName = account.accountName

        //Act
        val result1 = accountDao.get(account.accountID)?.accountName
        val result2 = accountDao.get("No Match")

        //Assert
        assertThat(result1, equalTo(expectedName))
        Assert.assertNull(result2)
    }

    @Test
    @Throws(Exception::class)
    fun remove() = runBlocking {
        //Arrange
        val account = accountsForUser2[5]
        val expectedSize = 9

        //Act
        accountDao.remove(account.accountID)
        val size = getValue(accountDao.getAll(user2)).size
        val fetchedAccount = accountDao.get(account.accountID)

        //Assert
        assertThat(size, equalTo(expectedSize))
        Assert.assertNull(fetchedAccount)
    }

    @Test
    @Throws(Exception::class)
    fun removeAll() = runBlocking {
        //Arrange
        val account = accountsForUser1[5]
        val expectedSize1 = 0
        val expectedSize2 = 10

        //Act
        accountDao.removeAll(user1)
        val size1 = getValue(accountDao.getAll(user1)).size
        val size2 = getValue(accountDao.getAll(user2)).size
        val fetchedAccount = accountDao.get(account.accountID)

        //Assert
        assertThat(expectedSize1, equalTo(size1))
        assertThat(expectedSize2, equalTo(size2))
        Assert.assertNull(fetchedAccount)
    }

    @Test
    @Throws(Exception::class)
    fun insert() = runBlocking {
        //Arrange
        val account = TestUtil.getAccount(20, user1)
        val expectedSize = 16
        val expectedName = account.accountName

        //Act
        accountDao.insert(account)
        val size = getValue(accountDao.getAll(user1)).size
        val fetchedAccount = accountDao.get(account.accountID)

        //Assert
        assertThat(size, equalTo(expectedSize))
        assertThat(fetchedAccount?.accountName, equalTo(expectedName))
    }

    @Test
    @Throws(Exception::class)
    fun update() = runBlocking {
        //Arrange
        val account = accountsForUser1[6]
        val newName = "Accounting"
        account.accountName = newName

        //Act
        accountDao.update(account)
        val fetchedAccount = accountDao.get(account.accountID)

        //Assert
        assertThat(fetchedAccount?.accountName, equalTo(newName))
    }

    @Test
    @Throws(Exception::class)
    fun search() {
        //Arrange
        val query1 = "%1%"
        val query2 = "%Account%"
        val query3 = "%nomatch%"
        val expectedSize1 = 7
        val expectedSize2 = 15
        val expectedSize3 = 0

        //Act
        val results1 = getValue(accountDao.search(query1, user1)).size
        val results2 = getValue(accountDao.search(query2, user1)).size
        val results3 = getValue(accountDao.search(query3, user1)).size

        //Assert
        assertThat(results1, equalTo(expectedSize1))
        assertThat(results2, equalTo(expectedSize2))
        assertThat(results3, equalTo(expectedSize3))
    }

    @Test
    @Throws(Exception::class)
    fun size() {
        //Arrange
        val expectedSize1 = 15
        val expectedSize2 = 10

        //Act
        val results1 = getValue(accountDao.size(user1))
        val results2 = getValue(accountDao.size(user2))

        //Assert
        assertThat(results1, equalTo(expectedSize1))
        assertThat(results2, equalTo(expectedSize2))
    }
}