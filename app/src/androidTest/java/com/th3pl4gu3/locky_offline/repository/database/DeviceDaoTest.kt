package com.th3pl4gu3.locky_offline.repository.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.TestUtil
import com.th3pl4gu3.locky_offline.core.credentials.Device
import com.th3pl4gu3.locky_offline.core.getValue
import com.th3pl4gu3.locky_offline.repository.database.daos.DeviceDao
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DeviceDaoTest {
    private lateinit var deviceDao: DeviceDao
    private lateinit var database: LockyDatabase
    private lateinit var devicesForUser1: List<Device>
    private lateinit var devicesForUser2: List<Device>
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
        deviceDao = database.deviceDao()

        /*
        * We have created two users, user1 & user 2
        * We add devices for each user separately
        * We then perform the tests
        */
        devicesForUser1 = TestUtil.createDevices(15, user1)
        devicesForUser2 = TestUtil.createDevices(10, user2)
        deviceDao.insertAll(devicesForUser1)
        deviceDao.insertAll(devicesForUser2)
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
        val devicesForUser1 = getValue(deviceDao.getAll(user1)) //Get all devices for user1
        val devicesForUser2 = getValue(deviceDao.getAll(user2)) //Get all devices for user2
        val resultSizeForUser1 = devicesForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = devicesForUser2.size //Fetch all for user 2

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
        deviceDao.removeAll(user1)
        deviceDao.removeAll(user2)
        /* Try to get now */
        val devicesForUser1 = getValue(deviceDao.getAll(user1)) //Get all devices for user1
        val devicesForUser2 = getValue(deviceDao.getAll(user2)) //Get all devices for user2
        val resultSizeForUser1 = devicesForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = devicesForUser2.size //Fetch all for user 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun getOne_DataExists() = runBlocking {
        /* Arrange */
        var deviceForUser1: Device? = null
        var deviceForUser2: Device? = null
        var resultForUser1: Device? = null
        var resultForUser2: Device? = null
        /* Act */
        val devicesForUser1 = getValue(deviceDao.getAll(user1)) //Get all devices for user1
        val devicesForUser2 = getValue(deviceDao.getAll(user2)) //Get all devices for user2
        deviceForUser1 = devicesForUser1.last() //Fetch last for user 1
        deviceForUser2 = devicesForUser2.last() //Fetch last for user 2
        /* Test single get */
        resultForUser1 = deviceDao.get(deviceForUser1.id)
        resultForUser2 = deviceDao.get(deviceForUser2.id)

        /* Assert */
        Assert.assertNotNull(resultForUser1)
        Assert.assertNotNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun getOne_DataDoesNotExists() = runBlocking {
        /* Arrange */
        var resultForUser1: Device? = null
        var resultForUser2: Device? = null

        /* Act */
        resultForUser1 = deviceDao.get(9846)
        resultForUser2 = deviceDao.get(5132)

        /* Assert */
        Assert.assertNull(resultForUser1)
        Assert.assertNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun removeOne_DataExists() = runBlocking {
        /* Arrange */
        var deviceForUser1: Device? = null
        var deviceForUser2: Device? = null
        var resultForUser1: Device? = null
        var resultForUser2: Device? = null

        /* Act */
        val devicesForUser1 = getValue(deviceDao.getAll(user1)) //Get all devices for user1
        val devicesForUser2 = getValue(deviceDao.getAll(user2)) //Get all devices for user2
        deviceForUser1 = devicesForUser1.last() //Fetch last for user 1
        deviceForUser2 = devicesForUser2.last() //Fetch last for user 2
        /* Test remove one */
        deviceDao.remove(deviceForUser1.id)
        deviceDao.remove(deviceForUser2.id)
        /* Try to fetch data now */
        resultForUser1 = deviceDao.get(deviceForUser1.id)
        resultForUser2 = deviceDao.get(deviceForUser2.id)

        /* Assert */
        Assert.assertNull(resultForUser1)
        Assert.assertNull(resultForUser2)
    }

    @Test
    @Throws(Exception::class)
    fun removeOne_DataDoesNotExists() = runBlocking {
        /* Arrange */
        var resultForUser1: Device? = null
        var resultForUser2: Device? = null

        /* Act */
        /* Remove object that doesn't exist */
        deviceDao.remove(65)
        deviceDao.remove(85)
        /* Try to fetch data now */
        resultForUser1 = deviceDao.get(65)
        resultForUser2 = deviceDao.get(65)

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
        deviceDao.removeAll(user1)
        deviceDao.removeAll(user2)
        /* Try to get now */
        val devicesForUser1 = getValue(deviceDao.getAll(user1)) //Get all devices for user1
        val devicesForUser2 = getValue(deviceDao.getAll(user2)) //Get all devices for user2
        val resultSizeForUser1 = devicesForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = devicesForUser2.size //Fetch all for user 2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun update_DataExists() = runBlocking {
        /* Arrange */
        var deviceForUser1: Device? = null
        var deviceForUser2: Device? = null
        val expectedNewEntryNameForUser1 = "EntryUser1"
        val expectedNewEntryNameForUser2 = "EntryUser2"

        /* Act */
        val devicesForUser1 = getValue(deviceDao.getAll(user1)) //Get all devices for user1
        val devicesForUser2 = getValue(deviceDao.getAll(user2)) //Get all devices for user2
        deviceForUser1 = devicesForUser1.last() //Fetch all for user 1
        deviceForUser2 = devicesForUser2.last() //Fetch all for user 2
        deviceForUser1.entryName = expectedNewEntryNameForUser1
        deviceForUser2.entryName = expectedNewEntryNameForUser2
        /* Test update */
        deviceDao.update(deviceForUser1)
        deviceDao.update(deviceForUser2)

        /* Assert */
        assertThat(
            deviceDao.get(deviceForUser1.id)?.entryName,
            equalTo(expectedNewEntryNameForUser1)
        )
        assertThat(
            deviceDao.get(deviceForUser2.id)?.entryName,
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
        deviceDao.update(
            Device()
                .apply { this.id = 89 })
        /* Try to get now */
        val devicesForUser1 = getValue(deviceDao.getAll(user1)) //Get all devices for user1
        val devicesForUser2 = getValue(deviceDao.getAll(user2)) //Get all devices for user2
        val resultSizeForUser1 = devicesForUser1.size //Fetch all for user 1
        val resultSizeForUser2 = devicesForUser2.size //Fetch all for user 2

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
        val resultSizeForUser1 = getValue(deviceDao.size(user1)) //Get all size for user1
        val resultSizeForUser2 = getValue(deviceDao.size(user2)) //Get all size for user2 2

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
        deviceDao.removeAll(user1)
        deviceDao.removeAll(user2)
        val resultSizeForUser1 = getValue(deviceDao.size(user1)) //Get all size for user1
        val resultSizeForUser2 = getValue(deviceDao.size(user2)) //Get all size for user2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

    @Test
    @Throws(Exception::class)
    fun search_DataExists_1Of2() = runBlocking {
        /* Arrange */
        val query = "%vice%"
        val expectedSizeForUser1 = 15 //Size for user1 only
        val expectedSizeForUser2 = 10 //Size for user2 only

        /* Act */
        val resultSizeForUser1 =
            getValue(deviceDao.search(query, user1)).size //Get all size for user1
        val resultSizeForUser2 =
            getValue(deviceDao.search(query, user2)).size //Get all size for user2

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
            getValue(deviceDao.search(query, user1)).size //Get all size for user1
        val resultSizeForUser2 =
            getValue(deviceDao.search(query, user2)).size //Get all size for user2

        /* Assert */
        assertThat(resultSizeForUser1, equalTo(expectedSizeForUser1))
        assertThat(resultSizeForUser2, equalTo(expectedSizeForUser2))
    }

}