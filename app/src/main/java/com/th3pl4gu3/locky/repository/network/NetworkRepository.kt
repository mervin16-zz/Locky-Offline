package com.th3pl4gu3.locky.repository.network

class NetworkRepository {
    private val apiService =
        RetrofitManager.retrofit

    suspend fun getWebsiteDetails(query: String) = apiService.getProperties(query)
}