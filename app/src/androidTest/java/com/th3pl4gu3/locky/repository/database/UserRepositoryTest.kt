package com.th3pl4gu3.locky.repository.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky.core.TestUtil
import com.th3pl4gu3.locky.core.getValue
import com.th3pl4gu3.locky.core.main.User
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UserRepositoryTest {
    private lateinit var userDao: UserDao
    private lateinit var database: Database
    private lateinit var user: User
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, Database::class.java
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
        val expectedName = user.name

        //Act
        val result = getValue(userDao.get(user.userID)).name

        //Assert
        ViewMatchers.assertThat(expectedName, CoreMatchers.equalTo(result))
    }

    @Test
    @Throws(Exception::class)
    fun remove() = runBlocking {
        //Arrange

        //Act
        userDao.remove(user.userID)
        val fetchedUser = getValue(userDao.get(user.userID))

        //Assert
        Assert.assertNull(fetchedUser)
    }

    @Test
    @Throws(Exception::class)
    fun update() = runBlocking {
        //Arrange
        val newName = "Usered"
        user.name = newName

        //Act
        userDao.update(user)
        val fetchedUser = getValue(userDao.get(user.userID))

        //Assert
        ViewMatchers.assertThat(newName, CoreMatchers.equalTo(fetchedUser.name))
    }
}