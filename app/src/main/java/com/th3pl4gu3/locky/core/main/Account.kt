package com.th3pl4gu3.locky.core.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(
    var accountID: String = "",
    var userID: String = "",
    var accountName: String = "",
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var logoUrl: String = "",
    var website: String? = null,
    var accountMoreInfo: String? = null,
    var twoFA: String? = null,
    var twoFASecretKeys: String? = null
) : Credentials(
    id = accountID,
    user = userID,
    name = accountName,
    additionalInfo = accountMoreInfo
), Parcelable {
    /*fun encrypt(){
        val encryptor = Encryptor()

        with(this){
            user = encryptor.encrypt(user)
            name = encryptor.encrypt(name)
            username = encryptor.encrypt(username)
            email = encryptor.encrypt(email)
            password = encryptor.encrypt(password)
            additionalInfo = if(!additionalInfo.isNullOrEmpty()) encryptor.encrypt(additionalInfo!!) else VALUE_SPACE
            twoFA = if(!twoFA.isNullOrEmpty()) encryptor.encrypt(twoFA!!) else VALUE_SPACE
            twoFASecretKeys = if(!twoFASecretKeys.isNullOrEmpty()) encryptor.encrypt(twoFASecretKeys!!) else VALUE_SPACE
            iv = encryptor.iv
        }
    }*/

    /*fun decrypt(){
        val decryptor = Decryptor()

        with(this){
            Log.i("ENCRYPTTEST", "User: $user IV: $iv")
            user = decryptor.decrypt(user, this.iv!!)
            Log.i("ENCRYPTTEST", "name")
            name = decryptor.decrypt(name, this.iv!!)
            Log.i("ENCRYPTTEST", "username")
            username = decryptor.decrypt(username, this.iv!!)
            Log.i("ENCRYPTTEST", "email")
            email = decryptor.decrypt(email, this.iv!!)
            Log.i("ENCRYPTTEST", "password")
            password = decryptor.decrypt(password, this.iv!!)
            Log.i("ENCRYPTTEST", "addinfo")
            additionalInfo = if(!additionalInfo.isNullOrEmpty()) decryptor.decrypt(additionalInfo!!, this.iv!!) else VALUE_SPACE
            Log.i("ENCRYPTTEST", "twofa")
            twoFA = if(!twoFA.isNullOrEmpty()) decryptor.decrypt(twoFA!!, this.iv!!) else VALUE_SPACE
            Log.i("ENCRYPTTEST", "secretkeys")
            twoFASecretKeys = if(!twoFASecretKeys.isNullOrEmpty()) decryptor.decrypt(twoFASecretKeys!!, this.iv!!) else VALUE_SPACE
            Log.i("ENCRYPTTEST", "iv")
            iv = null
            Log.i("ENCRYPTTEST", "Done")
        }
    }*/
}