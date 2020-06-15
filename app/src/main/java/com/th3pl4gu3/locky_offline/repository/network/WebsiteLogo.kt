package com.th3pl4gu3.locky_offline.repository.network

import com.squareup.moshi.Json

/*
* Data class to handle all logo details fetch from APIs
* Can hold the name, domain & url for the logo
*/
data class WebsiteLogo(
    val name: String,
    val domain: String,
    @Json(name = "logo") val logoUrl: String
)
