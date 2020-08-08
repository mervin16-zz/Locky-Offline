package com.th3pl4gu3.locky_offline.repository.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.TestUtil
import com.th3pl4gu3.locky_offline.core.credentials.BankAccount
import com.th3pl4gu3.locky_offline.core.getValue
import com.th3pl4gu3.locky_offline.repository.database.daos.BankAccountDao
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class BankAccountDaoTest {
    private lateinit var bankAccount: BankAccountDao
    private lateinit var database: LockyDatabase
    private lateinit var bankAccountsForUser1: List<BankAccount>
    private lateinit var bankAccountsForUser2: List<BankAccount>
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
        bankAccount = database.bankAccountDao()

        /*
        * We have created two users, user1 & user 2
        * We add bank accounts for each user separately
        * We then perform the tests
        */
        bankAccountsForUser1 = TestUtil.createBankAccounts(15, user1)
        bankAccountsForUser2 = TestUtil.createBankAccounts(10, user2)
        bankAccount.insertAll(bankAccountsForUser1)
        bankAccount.insertAll(bankAccountsForUser2)
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
        val bankAccountsForUser1 =
            getValue(bankAccount.getAll(user1)) //Get all bankAccounts for user1
        val bankAccountsForUser2 =
            getValue(bankAccount.getAll(user2)) //Get all bankAccounts for user2
        val resultSizeForUser1 = bankAccountsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = bankAccountsForUser2.size //Fetch all for user 2

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
        bankAccount.removeAll(user1)
        bankAccount.removeAll(user2)
        /* Try to get now */
        val bankAccountsForUser1 =
            getValue(bankAccount.getAll(user1)) //Get all bankAccounts for user1
        val bankAccountsForUser2 =
            getValue(bankAccount.getAll(user2)) //Get all bankAccounts for user2
        val resultSizeForUser1 = bankAccountsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = bankAccountsForUser2.size //Fetch all for user 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun getOne_DataExists() = runBlocking {
        /* Arrange */
        var bankAccountForUser1: BankAccount? = null
        var bankAccountForUser2: BankAccount? = null
        var resultForUser1: BankAccount? = null
        var resultForUser2: BankAccount? = null
        /* Act */
        val bankAccountsForUser1 =
            getValue(bankAccount.getAll(user1)) //Get all bankAccounts for user1
        val bankAccountsForUser2 =
            getValue(bankAccount.getAll(user2)) //Get all bankAccounts for user2
        bankAccountForUser1 = bankAccountsForUser1.last() //Fetch last for user 1
        bankAccountForUser2 = bankAccountsForUser2.last() //Fetch last for user 2
        /* Test single get */
        resultForUser1 = bankAccount.get(bankAccountForUser1.id)
        resultForUser2 = bankAccount.get(bankAccountForUser2.id)

        /* Assert */
        Assert.assertNotNull(resultForUser1)
        Assert.assertNotNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun getOne_DataDoesNotExists() = runBlocking {
        /* Arrange */
        var resultForUser1: BankAccount? = null
        var resultForUser2: BankAccount? = null

        /* Act */
        resultForUser1 = bankAccount.get(9846)
        resultForUser2 = bankAccount.get(5132)

        /* Assert */
        Assert.assertNull(resultForUser1)
        Assert.assertNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun removeOne_DataExists() = runBlocking {
        /* Arrange */
        var bankAccountForUser1: BankAccount? = null
        var bankAccountForUser2: BankAccount? = null
        var resultForUser1: BankAccount? = null
        var resultForUser2: BankAccount? = null

        /* Act */
        val bankAccountsForUser1 =
            getValue(bankAccount.getAll(user1)) //Get all bankAccounts for user1
        val bankAccountsForUser2 =
            getValue(bankAccount.getAll(user2)) //Get all bankAccounts for user2
        bankAccountForUser1 = bankAccountsForUser1.last() //Fetch last for user 1
        bankAccountForUser2 = bankAccountsForUser2.last() //Fetch last for user 2
        /* Test remove one */
        bankAccount.remove(bankAccountForUser1.id)
        bankAccount.remove(bankAccountForUser2.id)
        /* Try to fetch data now */
        resultForUser1 = bankAccount.get(bankAccountForUser1.id)
        resultForUser2 = bankAccount.get(bankAccountForUser2.id)

        /* Assert */
        Assert.assertNull(resultForUser1)
        Assert.assertNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun removeOne_DataDoesNotExists() = runBlocking {
        /* Arrange */
        var resultForUser1: BankAccount? = null
        var resultForUser2: BankAccount? = null

        /* Act */
        /* Remove object that doesn't exist */
        bankAccount.remove(65)
        bankAccount.remove(85)
        /* Try to fetch data now */
        resultForUser1 = bankAccount.get(65)
        resultForUser2 = bankAccount.get(65)

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
        bankAccount.removeAll(user1)
        bankAccount.removeAll(user2)
        /* Try to get now */
        val bankAccountsForUser1 =
            getValue(bankAccount.getAll(user1)) //Get all bankAccounts for user1
        val bankAccountsForUser2 =
            getValue(bankAccount.getAll(user2)) //Get all bankAccounts for user2
        val resultSizeForUser1 = bankAccountsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = bankAccountsForUser2.size //Fetch all for user 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun update_DataExists() = runBlocking {
        /* Arrange */
        var bankAccountForUser1: BankAccount? = null
        var bankAccountForUser2: BankAccount? = null
        val expectedNewEntryNameForUser1 = "EntryUser1"
        val expectedNewEntryNameForUser2 = "EntryUser2"

        /* Act */
        val bankAccountsForUser1 =
            getValue(bankAccount.getAll(user1)) //Get all bankAccounts for user1
        val bankAccountsForUser2 =
            getValue(bankAccount.getAll(user2)) //Get all bankAccounts for user2
        bankAccountForUser1 = bankAccountsForUser1.last() //Fetch all for user 1
        bankAccountForUser2 = bankAccountsForUser2.last() //Fetch all for user 2
        bankAccountForUser1.entryName = expectedNewEntryNameForUser1
        bankAccountForUser2.entryName = expectedNewEntryNameForUser2
        /* Test update */
        bankAccount.update(bankAccountForUser1)
        bankAccount.update(bankAccountForUser2)

        /* Assert */
        assertThat(
            bankAccount.get(bankAccountForUser1.id)?.entryName,
            equalTo(expectedNewEntryNameForUser1)
        )
        assertThat(
            bankAccount.get(bankAccountForUser2.id)?.entryName,
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
        bankAccount.update(
            BankAccount()
                .apply { this.id = 89 })
        /* Try to get now */
        val bankAccountsForUser1 =
            getValue(bankAccount.getAll(user1)) //Get all bankAccounts for user1
        val bankAccountsForUser2 =
            getValue(bankAccount.getAll(user2)) //Get all bankAccounts for user2
        val resultSizeForUser1 = bankAccountsForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = bankAccountsForUser2.size //Fetch all for user 2

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
        val resultSizeForUser1 = getValue(bankAccount.size(user1)) //Get all size for user1
        val resultSizeForUser2 = getValue(bankAccount.size(user2)) //Get all size for user2 2

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
        bankAccount.removeAll(user1)
        bankAccount.removeAll(user2)
        val resultSizeForUser1 = getValue(bankAccount.size(user1)) //Get all size for user1
        val resultSizeForUser2 = getValue(bankAccount.size(user2)) //Get all size for user2

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
            getValue(bankAccount.search(query, user1)).size //Get all size for user1
        val resultSizeForUser2 =
            getValue(bankAccount.search(query, user2)).size //Get all size for user2

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
            getValue(bankAccount.search(query, user1)).size //Get all size for user1
        val resultSizeForUser2 =
            getValue(bankAccount.search(query, user2)).size //Get all size for user2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

}