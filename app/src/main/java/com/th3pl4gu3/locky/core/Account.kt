package com.th3pl4gu3.locky.core

data class Account(override val id: String) : Credentials(){

   var username: String? = null
      internal set
   var email: String? = null
      internal set
   var password: String = ""
      internal set
   var website: String? = null
      internal set
   var isTwoFA: Boolean = false
      internal set
   var twoFASecretKeys: String? = null
      internal set

}