package com.th3pl4gu3.locky.security

import com.th3pl4gu3.locky.security.Constants.Companion.EXCEPTION_HASH_NOT_VALID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class HashTest {

    @ParameterizedTest
    @CsvSource(
        "This is a message,a826c7e389ec9f379cafdc544d7e9a4395ff7bfb58917bbebee51b3d0b1c996a",
        "'  another m3ss4g3 32 23   ',d1e49e2d38ee0bfc82162a414505f36d3d2696274f00e13801d2ad2bae6db6c6",
        "' a cOmpLic4t3d mesa@ge é 4↨ @',573558065ca0da7f86f82ec1b06cadcba310876d3c681589fb9867b68019b478"
    )
    fun sha256HashTests(message: String, expectedResult: String?) {
        val result: String?

        //Act
        result = Hash.hash(message)

        //Assert
        assertEquals(expectedResult, result)
    }

    @ParameterizedTest
    @CsvSource(
        "This is a message,b6863e5356bc52ff70597140646b3563e16babd2a5c589ad4784127bb64781a9",
        "'  another m3ss4g3 32 23   ',e16eda960707f301b0e6ece81e8ae21154c9ffdbbdfb201fbc0e81cb28fa227e",
        "' a cOmpLic4t3d mesa@ge é 4↨ @',ec198e831af29a940c9ac6941359dd0416a14d7592507e47788fa619eb0b95d5"
    )
    fun sha256DoubleHashTests(message: String, expectedResult: String?) {
        val result: String?

        //Act
        result = Hash.doubleHash(message)

        //Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun isHashValidWhenHashIsValid() {
        //Arrange
        val message = "This is a message."
        val hash = "a3964890912366008dee9864a4dfddf88446f354b989e340f826e21b2e83bd9c"
        val expectedResult = "Passed"
        var result: String?

        //Act
        try{
            Hash.validateHash(message, hash)
            result = expectedResult
        }catch(e: Exception){
            result = e.message
        }

        //Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun isHashValidWhenHashIsNotValid() {
        //Arrange
        val message = "This is a message."
        val hash = "354b989e340f826e21b2e83bd9c"
        val expectedResult = EXCEPTION_HASH_NOT_VALID
        var result: String?

        //Act
        try{
            Hash.validateHash(message, hash)
            result = expectedResult
        }catch(e: Exception){
            result = e.message
        }

        //Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun isDoubleHashValidWhenDoubleHashIsValid() {
        //Arrange
        val message = "This is a message."
        val hash = "635049416fd0bb2f2959fd97c0dcd1f22c7ce3a065b18479045900e0dc3df3de"
        val expectedResult = "Passed"
        var result: String?

        //Act
        try{
            Hash.validateDoubleHash(message, hash)
            result = expectedResult
        }catch(e: Exception){
            result = e.message
        }

        //Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun isDoubleHashValidWhenDoubleHashIsNotValid() {
        //Arrange
        val message = " a cOmpLic4t3d mesa@ge é 4↨ @"
        val hash = "e47788fa619eb0b95d5"
        val expectedResult = EXCEPTION_HASH_NOT_VALID
        var result: String?

        //Act
        try{
            Hash.validateDoubleHash(message, hash)
            result = expectedResult
        }catch(e: Exception){
            result = e.message
        }

        //Assert
        assertEquals(expectedResult, result)
    }
}