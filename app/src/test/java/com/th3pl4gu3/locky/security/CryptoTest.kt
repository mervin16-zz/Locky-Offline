package com.th3pl4gu3.locky.security

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CryptoTest{
    @ParameterizedTest
    @ValueSource(strings = ["Mervin", "this is a message", " this is c0mplicat3e @", "35dfsdfs ##$ ..↨é", "P~12    #"])
    fun aesCipherTests(message: String) {
        //Arrange
        val encryptedMessage: ByteArray?
        val decryptedMessage: ByteArray?

        //Act
        val sutCrypto = Crypto()
        encryptedMessage = sutCrypto.encrypt(message.toByteArray())
        decryptedMessage = sutCrypto.decrypt(encryptedMessage)

        //Print
        assertEquals(message, String(decryptedMessage!!))
    }
}