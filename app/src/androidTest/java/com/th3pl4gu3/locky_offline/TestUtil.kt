package com.th3pl4gu3.locky_offline.core

import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.core.main.User

class TestUtil {

    companion object {
        fun getAccount(number: Int) = Account(
            accountName = "Account $number",
            username = "Username $number",
            email = "Email $number",
            logoUrl = "www.logo.com/$number",
            website = "www.account.com/$number",
            password = "Password $number",
            authenticationType = "Auth $number",
            twoFASecretKeys = "Keys $number",
            accountMoreInfo = "More Info $number"
        )

        fun createAccounts(size: Int) = ArrayList<Account>().apply {
            for (number in 1..size) {
                add(
                    Account(
                        accountName = "Account $number",
                        username = "Username $number",
                        email = "Email $number",
                        logoUrl = "www.logo.com/$number",
                        website = "www.account.com/$number",
                        password = "Password $number",
                        authenticationType = "Auth $number",
                        twoFASecretKeys = "Keys $number",
                        accountMoreInfo = "More Info $number"
                    )
                )
            }
        }

        fun getUser(number: Int) = User(
            name = "User $number",
            photo = "Photo $number",
            email = "Email $number"
        )

        fun getCard(number: Int) = Card(
            entryName = "Card $number",
            number = "Number $number",
            pin = "Pin $number",
            bank = "Bank $number",
            expiryDate = "Expiry $number",
            issuedDate = "Issued $number",
            cardHolderName = "Cardholder $number",
            cardMoreInfo = "More Info $number"
        )

        fun createCards(size: Int) = ArrayList<Card>().apply {
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
                        cardMoreInfo = "More Info $number"
                    )
                )
            }
        }
    }
}