package com.th3pl4gu3.locky.core.cryptography

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


class Encryptor {

    companion object {
        private const val TAG = "ENCRYPTION_PROCESS"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val ALIAS = "MYALIAS"
    }

    private lateinit var _encryption: ByteArray
    private lateinit var _iv: String

    val iv: String
        get() = _iv


    fun encrypt(message: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        _iv = String(encode(cipher.iv))
        _encryption = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return String(encode(_encryption))
    }

    private fun encode(data: ByteArray): ByteArray = Base64.getEncoder().encode(data)

    private fun getSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator
            .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)

        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
        )

        return keyGenerator.generateKey()
    }
}