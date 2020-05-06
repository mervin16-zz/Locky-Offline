package com.th3pl4gu3.locky.core

class Constants {
    companion object {

        /*
            ****    Values    ****
         */
        const val VALUE_FORMAT_SPECIFIER = "%02x"
        const val VALUE_SPACE = ""
        const val VALUE_HASH_SHA256 = "SHA-256"
        const val VALUE_CIPHER_IV_SIZE = 16
        const val VALUE_CIPHER_AES_KEY_SIZE = 256
        const val VALUE_CIPHER_AES = "AES"
        const val VALUE_CIPHER_AESCBCPKCS5 = "AES/CBC/PKCS5PADDING"


        /*
            ****    Error Messages | Exceptions    ****
         */
        const val EXCEPTION_HASH_NOT_VALID = "Hash is not valid"
        const val EXCEPTION_HASH_DIGEST = "Couldn't make digest of partial content"
        const val EXCEPTION_GENERAL = "An error occurred in the system. Please try again later"
        const val EXCEPTION_FORM = "There are field errors"
        const val ERROR_MESSAGE_FIELD_CANNOT_BE_EMPTY = "This field cannot be empty"
        const val ERROR_MESSAGE_EMAIL_FORMAT = "Please enter a valid email format"
    }
}