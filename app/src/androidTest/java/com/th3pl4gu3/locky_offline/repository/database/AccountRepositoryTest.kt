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
        val expectedSize = 15

        //Act
        val result = getValue(accountDao.getAll(user1)).size

        //Assert
        assertThat(result, equalTo(expectedSize))
    }

    @Test
    @Throws(Exception::class)
    fun get() = runBlocking {
        //Arrange
        val account = accountsForUser1[5]
        val expectedName = account.accountName

        //Act
        val result = accountDao.get(account.accountID)?.accountName

        //Assert
        assertThat(result, equalTo(expectedName))
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

}