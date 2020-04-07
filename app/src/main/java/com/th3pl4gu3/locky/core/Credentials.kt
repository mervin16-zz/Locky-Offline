package com.th3pl4gu3.locky.core

import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

abstract class Credentials {

    internal abstract val id: String
    open var name: String = ""
        internal set

    internal var secretKeySpec: SecretKeySpec? = null
    internal var ivParameterSpec: IvParameterSpec? = null
    internal var additionalInfo: String? = null
}