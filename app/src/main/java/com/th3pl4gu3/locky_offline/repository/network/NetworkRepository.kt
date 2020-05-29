package com.th3pl4gu3.locky_offline.repository.network

class NetworkRepository {
    private val apiService =
        RetrofitManager.retrofit

    suspend fun getWebsiteDetails(query: String) =
        if (query.isEmpty()) ArrayList() else apiService.getProperties(query)
}