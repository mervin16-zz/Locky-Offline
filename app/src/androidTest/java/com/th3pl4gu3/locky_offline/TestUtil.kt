package com.th3pl4gu3.locky_offline

import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.repository.billing.AugmentedSkuDetails

object TestUtil {
    fun createAugmentedSkuDetails(size: Int) = ArrayList<AugmentedSkuDetails>().apply {
        for (number in 1..size) {
            add(
                AugmentedSkuDetails(
                    purchased = true,
                    sku = "cookie$number",
                    type = "COOKIE$number",
                    price = "$$number.00",
                    title = "Cookie$number",
                    description = "This is a cookie$number",
                    originalJson = "{'title':'Cookie$number'}"
                )
            )
        }
    }

    fun getAugmentedSkuDetails(number: Int) =
        AugmentedSkuDetails(
            purchased = true,
            sku = "cookie$number",
            type = "COOKIE$number",
            price = "$$number.00",
            title = "Cookie$number",
            description = "This is a cookie$number",
            originalJson = "{'title':'Cookie$number'}"
        )


    fun getAccount(number: Int, user: String) = Account(
        entryName = "Account $number",
        username = "Username $number",
        email = "www.myemail$number.com",
        logoUrl = "www.logo.com/$number",
        website = "www.account.com/$number",
        password = "Password $number",
        user = user,
        authenticationType = "Auth $number",
        twoFASecretKeys = "Keys $number",
        additionalInfo = "More Info $number"
    )

    fun createAccounts(size: Int, user: String) = ArrayList<Account>().apply {
        for (number in 1..size) {
            add(
                Account(
                    entryName = "Account $number",
                    username = "Username $number",
                    email = "www.myemail$number.com",
                    logoUrl = "www.logo.com/$number",
                    website = "www.account.com/$number",
                    password = "Password $number",
                    user = user,
                    authenticationType = "Auth $number",
                    twoFASecretKeys = "Keys $number",
                    additionalInfo = "More Info $number"
                )
            )
        }
    }

    fun getUser(number: Int) = User(
        name = "User $number",
        photo = "Photo $number",
        email = "www.useremail$number.com"
    )

    fun getCard(number: Int, user: String) = Card(
        entryName = "Card $number",
        number = "Number $number",
        pin = "Pin $number",
        bank = "Bank $number",
        expiryDate = "Expiry $number",
        issuedDate = "Issued $number",
        cardHolderName = "Cardholder $number",
        user = user,
        additionalInfo = "More Info $number"
    )

    fun createCards(size: Int, user: String) = ArrayList<Card>().apply {
        for (number in 1..size) {
            add(
                Card(
                    entryName = "Card $number",
                    number = "Number $number",
                    pin = "Pin $number",
                    bank = "Bank $number",
                    expiryDate = "Expiry $number",
                    issuedDate = "Issued $number",
                    cardHolderName = "Cardholder $number",
                    user = user,
                    additionalInfo = "More Info $number"
                )
            )
        }
    }

}