package com.th3pl4gu3.locky.core

data class Account(override val id: String) : Credentials(){

    var name: String = ""
    var username: String? = null
    var email: String? = null
    var password: String = ""
    var website: String? = null
    var isTwoFA: Boolean = false
    var twoFASecretKeys: String? = null

}