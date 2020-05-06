package com.th3pl4gu3.locky.core.cryptography

import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class Decryptor {

    companion object {
        private const val TAG = "DECRYPTION_PROCESS"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val ALIAS = "MYALIAS"
    }

    private val _keyStore: KeyStore

    init {
        //Load the keystore
        _keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        _keyStore.load(null)
    }

    fun decrypt(encryptedData: String, encryptionIv: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec =
            GCMParameterSpec(128, decode(encryptionIv.toByteArray()))
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)

        return String(cipher.doFinal(decode(encryptedData.toByteArray())), Charsets.UTF_8)
    }

    private fun decode(data: ByteArray): ByteArray = Base64.getDecoder().decode(data)

    private fun getSecretKey(): SecretKey =
        (_keyStore.getEntry(ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
}