package com.th3pl4gu3.locky_offline.ui.main.view

interface ViewCredentialListener {
    fun onCopyClicked(data: String)
    fun onViewClicked(data: String) {}
    fun onLinkClicked(data: String) {}
    fun onShareClicked(data: String) {}
}