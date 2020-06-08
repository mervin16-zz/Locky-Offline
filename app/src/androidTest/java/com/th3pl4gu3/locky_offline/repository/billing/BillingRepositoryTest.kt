package com.th3pl4gu3.locky_offline.repository.billing

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.th3pl4gu3.locky_offline.TestUtil
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
internal class BillingRepositoryTest {

    private lateinit var donationDao: DonationDao
    private lateinit var database: BillingDatabase
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, BillingDatabase::class.java
        ).build()
        donationDao = database.donationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun verifyAugmentedSku_WhenNoPreviousRecordsExist() = runBlocking {
        //Arrange
        val fakeSku = "cookie1"

        //Act
        val exists = donationDao.getAugmentedSkuDetails(fakeSku) != null

        //Assert
        Assert.assertFalse(exists)
    }

    @Test
    @Throws(Exception::class)
    fun verifyAugmentedSku_WhenPreviousRecordsExist_RealSku() = runBlocking {
        //Arrange
        val augmentedSkuDetails1 = TestUtil.getAugmentedSkuDetails(1)
        val augmentedSkuDetails2 = TestUtil.getAugmentedSkuDetails(2)
        val augmentedSkuDetails3 = TestUtil.getAugmentedSkuDetails(3)

        //Act
        donationDao.insert(augmentedSkuDetails1)
        donationDao.insert(augmentedSkuDetails2)
        donationDao.insert(augmentedSkuDetails3)
        val exists = donationDao.getAugmentedSkuDetails(augmentedSkuDetails2.sku) != null

        //Assert
        Assert.assertTrue(exists)
    }

    @Test
    @Throws(Exception::class)
    fun verifyAugmentedSku_WhenPreviousRecordsExist_FakeSku() = runBlocking {
        //Arrange
        val fakeSku = "asdas"
        val augmentedSkuDetails1 = TestUtil.getAugmentedSkuDetails(1)
        val augmentedSkuDetails2 = TestUtil.getAugmentedSkuDetails(2)
        val augmentedSkuDetails3 = TestUtil.getAugmentedSkuDetails(3)

        //Act
        donationDao.insert(augmentedSkuDetails1)
        donationDao.insert(augmentedSkuDetails2)
        donationDao.insert(augmentedSkuDetails3)
        val exists = donationDao.getAugmentedSkuDetails(fakeSku) != null

        //Assert
        Assert.assertFalse(exists)
    }

    @Test
    @Throws(Exception::class)
    fun insertAugmentedSku_WhenNoPreviousRecordsExist() = runBlocking {
        //Arrange
        val augmentedSkuDetails = TestUtil.getAugmentedSkuDetails(1)

        //Act
        donationDao.insert(augmentedSkuDetails)
        val exists = donationDao.getAugmentedSkuDetails(augmentedSkuDetails.sku) != null

        //Assert
        Assert.assertTrue(exists)
    }

    /*@Test
    @Throws(Exception::class)
    fun getCookie_NoCookiePurchasedBefore() = runBlocking {
        //Arrange

        //Act
        val hasPurchase = donationDao.getCookie()
        val exists = donationDao.exists(Cookie(true))

        //Assert
        Assert.assertNull(hasPurchase)
        Assert.assertFalse(exists)
    }

    @Test
    @Throws(Exception::class)
    fun purchaseCookieDonation_NoCookiePurchasedBefore() = runBlocking {
        //Arrange
        val cookie = Cookie(true)

        //Act
        donationDao.insert(cookie)
        val hasPurchase = donationDao.getCookie()?.hasPurchased
        val exists = donationDao.exists(cookie)

        //Assert
        Assert.assertTrue(hasPurchase!!)
        Assert.assertTrue(exists)
    }

    @Test
    @Throws(Exception::class)
    fun purchaseCookieDonation_AlreadyPurchasedBefore() = runBlocking {
        //Arrange
        val cookie = Cookie(true)

        //Act
        donationDao.insert(cookie)
        donationDao.insert(cookie)
        val hasPurchase = donationDao.getCookie()?.hasPurchased
        val exists = donationDao.exists(cookie)

        //Assert
        Assert.assertTrue(hasPurchase!!)
        Assert.assertTrue(exists)
    }

    @Test
    @Throws(Exception::class)
    fun purchaseCookieAndSandwichDonation_CookieAlreadyPurchasedBefore() = runBlocking {
        //Arrange
        val cookie = Cookie(true)
        val sandwich = Sandwich(true)

        //Act
        donationDao.insert(cookie)
        donationDao.insert(sandwich)
        val hasPurchase = donationDao.getCookie()?.hasPurchased
        val cookieExists = donationDao.exists(cookie)
        val sandwichExists = donationDao.exists(sandwich)

        //Assert
        Assert.assertTrue(hasPurchase!!)
        Assert.assertTrue(cookieExists)
        Assert.assertTrue(sandwichExists)
    }*/
}