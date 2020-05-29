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
    private lateinit var accounts: List<Account>

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, Database::class.java
        ).build()
        accountDao = database.accountDao()

        /* Add accounts to the database beforehand */
        accounts = TestUtil.createAccounts(10)
        accountDao.insertAll(accounts)
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
        val result = getValue(accountDao.getAll()).size

        //Assert
        assertThat(expectedSize, equalTo(result))
    }

    @Test
    @Throws(Exception::class)
    fun get() {
        //Arrange
        val account = accounts[5]
        val expectedName = account.accountName

        //Act
        val result = getValue(accountDao.get(account.accountID)).accountName

        //Assert
        assertThat(expectedName, equalTo(result))
    }

    @Test
    @Throws(Exception::class)
    fun remove() = runBlocking {
        //Arrange
        val account = accounts[5]
        val expectedSize = 9

        //Act
        accountDao.remove(account.accountID)
        val size = getValue(accountDao.getAll()).size
        val fetchedAccount = getValue(accountDao.get(account.accountID))

        //Assert
        assertThat(expectedSize, equalTo(size))
        Assert.assertNull(fetchedAccount)
    }

    @Test
    @Throws(Exception::class)
    fun removeAll() = runBlocking {
        //Arrange
        val account = accounts[5]
        val expectedSize = 0
        val expectedName = account.accountName

        //Act
        accountDao.removeAll()
        val size = getValue(accountDao.getAll()).size
        val fetchedAccount = getValue(accountDao.get(account.accountID))

        //Assert
        assertThat(expectedSize, equalTo(size))
        Assert.assertNull(fetchedAccount)
    }

    @Test
    @Throws(Exception::class)
    fun insert() = runBlocking {
        //Arrange
        val account = TestUtil.getAccount(20)
        val expectedSize = 11
        val expectedName = account.accountName

        //Act
        accountDao.insert(account)
        val size = getValue(accountDao.getAll()).size
        val fetchedAccount = getValue(accountDao.get(account.accountID))

        //Assert
        assertThat(expectedSize, equalTo(size))
        assertThat(expectedName, equalTo(fetchedAccount.accountName))
    }

    @Test
    @Throws(Exception::class)
    fun update() = runBlocking {
        //Arrange
        val account = accounts[6]
        val newName = "Accounting"
        account.accountName = newName

        //Act
        accountDao.update(account)
        val fetchedAccount = getValue(accountDao.get(account.accountID))

        //Assert
        assertThat(newName, equalTo(fetchedAccount.accountName))
    }

}