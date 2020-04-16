package com.th3pl4gu3.locky.core.networking

import com.th3pl4gu3.locky.core.networking.RetrofitManager

class Repository {
    private val apiService = RetrofitManager.retrofit

    suspend fun getWebsiteDetails() = apiService.getProperties("face")
}