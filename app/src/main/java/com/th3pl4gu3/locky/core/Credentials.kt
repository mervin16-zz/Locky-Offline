package com.th3pl4gu3.locky.core

import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

abstract class Credentials (
    id: String = "",
    name: String = "",
    additionalInfo: String?
) {
    internal var secretKeySpec: SecretKeySpec? = null
    internal var ivParameterSpec: IvParameterSpec? = null
}