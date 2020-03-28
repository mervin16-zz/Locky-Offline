package com.th3pl4gu3.locky.security

import com.th3pl4gu3.locky.security.Constants.Companion.VALUE_CIPHER_AES
import com.th3pl4gu3.locky.security.Constants.Companion.VALUE_CIPHER_AESCBCPKCS5
import com.th3pl4gu3.locky.security.Constants.Companion.VALUE_CIPHER_AES_KEY_SIZE
import com.th3pl4gu3.locky.security.Constants.Companion.VALUE_CIPHER_IV_SIZE
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Crypto {

    internal val keySpec: SecretKeySpec
    internal val ivSpec: IvParameterSpec

    init{
        //Generate Key
        val keyGenerator = KeyGenerator.getInstance(VALUE_CIPHER_AES)
        keyGenerator.init(VALUE_CIPHER_AES_KEY_SIZE)
        keySpec = SecretKeySpec((keyGenerator.generateKey()).encoded, VALUE_CIPHER_AES)

        //Generate IV
        val iv = ByteArray(VALUE_CIPHER_IV_SIZE)
        SecureRandom().nextBytes(iv)
        ivSpec = IvParameterSpec(iv)
    }

    @Throws(Exception::class)
    fun encrypt(plaintext: ByteArray?): ByteArray? {
        val cipher = Cipher.getInstance(VALUE_CIPHER_AESCBCPKCS5)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        return cipher.doFinal(plaintext)
    }

    @Throws(Exception::class)
    fun decrypt(cipherText: ByteArray?): ByteArray? {
        val cipher = Cipher.getInstance(VALUE_CIPHER_AESCBCPKCS5)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        return cipher.doFinal(cipherText)
    }
}