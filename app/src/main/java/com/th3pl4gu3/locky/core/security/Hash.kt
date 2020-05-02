package com.th3pl4gu3.locky.core.security

import com.th3pl4gu3.locky.core.security.Constants.Companion.EXCEPTION_GENERAL
import com.th3pl4gu3.locky.core.security.Constants.Companion.EXCEPTION_HASH_DIGEST
import com.th3pl4gu3.locky.core.security.Constants.Companion.EXCEPTION_HASH_NOT_VALID
import com.th3pl4gu3.locky.core.security.Constants.Companion.VALUE_FORMAT_SPECIFIER
import com.th3pl4gu3.locky.core.security.Constants.Companion.VALUE_HASH_SHA256
import com.th3pl4gu3.locky.core.security.Constants.Companion.VALUE_SPACE
import java.security.MessageDigest

class Hash {

    companion object{
        fun hash(message: String): String{
            return digest(message)
        }

        fun doubleHash(message: String): String{
            return digest((digest(message) + message))
        }

        @Throws(Exception::class)
        fun validateHash(original: String, hashed: String){
            if(hash(original) != hashed){
                throw Exception(EXCEPTION_HASH_NOT_VALID)
            }
        }

        @Throws(Exception::class)
        fun validateDoubleHash(original: String, hashed: String){
            if(doubleHash(original) != hashed){
                throw Exception(EXCEPTION_HASH_NOT_VALID)
            }
        }

        private fun digest(message: String): String{
            return try{
                MessageDigest
                    .getInstance(VALUE_HASH_SHA256)
                    .digest(message.toByteArray())
                    .fold(VALUE_SPACE, { str, it -> str + VALUE_FORMAT_SPECIFIER.format(it) })
            }catch (e: CloneNotSupportedException) {
                EXCEPTION_HASH_DIGEST
            }catch (e: Exception) {
                EXCEPTION_GENERAL
            }
        }
    }

}