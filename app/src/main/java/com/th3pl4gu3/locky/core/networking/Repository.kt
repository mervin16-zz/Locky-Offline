package com.th3pl4gu3.locky.core.networking

class Repository {
    private val apiService = RetrofitManager.retrofit

    suspend fun getWebsiteDetails(query: String) = apiService.getProperties(query)
}