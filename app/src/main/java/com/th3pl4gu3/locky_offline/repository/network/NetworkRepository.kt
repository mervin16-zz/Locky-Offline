package com.th3pl4gu3.locky_offline.repository.network

import android.app.Application
import com.th3pl4gu3.locky_offline.repository.billing.BillingRepository

/*
* Repository pattern for network requests
* Network request currently supports only fetching logos
*/
class NetworkRepository private constructor(){

    private val apiService =
        RetrofitManager.retrofit

    companion object {
        @Volatile
        private var INSTANCE: NetworkRepository? = null

        fun getInstance(): NetworkRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: NetworkRepository()
                        .also { INSTANCE = it }
            }
    }

    /*
    * This method gets Logo directly from
    * Logo API Clearbit [https://clearbit.com/logo]
    * Clearbit is a free logo api that performs name to domain logo resolution
    * User just needs to perfor ma search and this query will match
    * at most 5 logo requests
    */
    suspend fun getWebsiteDetails(query: String) =
        if (query.isEmpty()) ArrayList() else apiService.getProperties(query)


    /*
    * This method gets the logo from
    * Logo API Uplead [https://www.uplead.com/free-company-logo-api/]
    * Uplead is a free logo api that will only return the logo url
    * We then construct the method so that it returns only 1 WebsiteLogo object
    */
    fun getWebsiteDetail(query: String) = if (query.isNotEmpty()) WebsiteLogo(
        name = query,
        domain = query,
        logoUrl = "https://logo.uplead.com/$query"
    ) else null
}