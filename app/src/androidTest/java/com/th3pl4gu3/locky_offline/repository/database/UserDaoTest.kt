package com.th3pl4gu3.locky_offline.repository.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.TestUtil
import com.th3pl4gu3.locky_offline.core.others.User
import com.th3pl4gu3.locky_offline.repository.database.daos.UserDao
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var userDao: UserDao
    private lateinit var database: LockyDatabase
    private lateinit var user: User

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, LockyDatabase::class.java
        ).build()
        userDao = database.userDao()

        /* Add a user for testing purpose */
        user = TestUtil.getUser(10)
        userDao.insert(user)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun get() = runBlocking {
        //Arrange
        val expectedEmail = user.email

        //Act
        val result = userDao.get(user.email)?.email

        //Assert
        assertThat(result, equalTo(expectedEmail))
    }

    @Test
    @Throws(Exception::class)
    fun insert() = runBlocking {
        //Arrange
        val user = TestUtil.getUser(10)

        //Act
        userDao.insert(user)
        val fetchedUser = userDao.get(user.email)

        //Assert
        assertThat(user.email, equalTo(user.email))
    }

    @Test
    @Throws(Exception::class)
    fun remove() = runBlocking {
        //Arrange

        //Act
        userDao.remove(user.email)
        val fetchedUser = userDao.get(user.email)

        //Assert
        Assert.assertNull(fetchedUser)
    }
}