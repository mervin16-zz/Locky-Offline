package com.th3pl4gu3.locky.core

import com.google.firebase.database.Exclude
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

abstract class Credentials (
    open var id: String = "",
    user: String = "",
    name: String = "",
    additionalInfo: String?
) {

    @set:Exclude @get:Exclude internal var secretKeySpec: SecretKeySpec? = null

    @set:Exclude @get:Exclude internal var ivParameterSpec: IvParameterSpec? = null
}