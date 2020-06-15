package com.th3pl4gu3.locky_offline.repository.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/*
* Retrofit API service to fetch LOGO details from API
* Current Logo API configured: [clearbit]
* Users can pass a search query only and the logo will be returned
*/
private const val BASE_URL = "https://autocomplete.clearbit.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitService = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("v1/companies/suggest?")
    suspend fun getProperties(@Query("query") website: String): List<WebsiteLogo>
}

object RetrofitManager {

    val retrofit: ApiService by lazy {
        retrofitService
        .create(ApiService::class.java)
    }

}


