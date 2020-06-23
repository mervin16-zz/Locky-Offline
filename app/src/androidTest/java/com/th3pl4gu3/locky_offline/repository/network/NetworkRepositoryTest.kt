package com.th3pl4gu3.locky_offline.repository.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NetworkRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    @Throws(Exception::class)
    fun size() = runBlocking {
        //Arrange
        val query = "link"
        val expectedSize = 5

        //Act
        val result = NetworkRepository.getInstance().getWebsiteDetails(query).size

        //Assert
        assertThat(expectedSize, equalTo(result))
    }

    @Test
    @Throws(Exception::class)
    fun query() = runBlocking {
        //Arrange
        val query = "google"
        val expectedName = "Google"
        val expectedDomain = "google.com"
        val expectedLogo = "https://logo.clearbit.com/google.com"

        //Act
        val result = NetworkRepository.getInstance().getWebsiteDetails(query).filter {
            it.domain == expectedDomain
        }[0]

        //Assert
        assertThat(expectedName, equalTo(result.name))
        assertThat(expectedDomain, equalTo(result.domain))
        assertThat(expectedLogo, equalTo(result.logoUrl))
    }

    @Test
    @Throws(Exception::class)
    fun emptyQuery() = runBlocking {
        //Arrange
        val query = ""
        val expectedSize = 0

        //Act
        val result = NetworkRepository.getInstance().getWebsiteDetails(query).size

        //Assert
        assertThat(expectedSize, equalTo(result))
    }

    @Test
    @Throws(Exception::class)
    fun irrelevantQuery() = runBlocking {
        //Arrange
        val query = "423j42342j34k23j4234j234"
        val expectedSize = 0

        //Act
        val result = NetworkRepository.getInstance().getWebsiteDetails(query).size

        //Assert
        assertThat(expectedSize, equalTo(result))
    }
}