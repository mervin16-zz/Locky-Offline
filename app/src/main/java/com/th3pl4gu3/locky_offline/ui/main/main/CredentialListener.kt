package com.th3pl4gu3.locky_offline.ui.main.main

import android.view.View
import com.th3pl4gu3.locky_offline.core.credentials.Credentials

interface CredentialListener {
    fun onCredentialClicked(credential: Credentials)
    fun onViewClicked(view: View, credential: Credentials) {}
    fun onCredentialLongPressed(credential: Credentials): Boolean = false
}