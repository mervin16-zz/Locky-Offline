package com.th3pl4gu3.locky_offline.ui.main.main

import com.th3pl4gu3.locky_offline.core.main.credentials.Credentials

interface CredentialListener {
    fun onCredentialClicked(credential: Credentials)
    fun onViewClicked(credential: Credentials) {}
    fun onCredentialLongPressed(credential: Credentials): Boolean = false
}