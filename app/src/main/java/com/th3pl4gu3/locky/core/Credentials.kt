package com.th3pl4gu3.locky.core

import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

abstract class Credentials {

    abstract val id: String

    var secretKeySpec: SecretKeySpec? = null
    var ivParameterSpec: IvParameterSpec? = null
    var additionalInfo: String? = null
}