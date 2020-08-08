package com.th3pl4gu3.locky_offline.ui.main.utils

import com.th3pl4gu3.locky_offline.core.credentials.Account
import com.th3pl4gu3.locky_offline.core.credentials.BankAccount
import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.core.others.User
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


    fun getAccount(number: Int, user: String) = Account()
        .apply {
            this.entryName = "Account $number"
            this.username = "Username $number"
            this.email = "myemail$number@email.com"
            this.logoUrl = "www.logo.com/$number"
            this.website = "www.account.com/$number"
            this.password = "Password $number"
            this.user = user
            this.authenticationType = "Auth $number"
            this.twoFASecretKeys = "Keys $number"
            this.additionalInfo = "More Info $number"
    }

    fun createAccounts(size: Int, user: String) = ArrayList<Account>().apply {
        for (number in 1..size) {
            add(
                Account().apply {
                    this.entryName = "Account $number"
                    this.username = "Username $number"
                    this.email = "www.myemail$number.com"
                    this.logoUrl = "www.logo.com/$number"
                    this.website = "www.account.com/$number"
                    this.password = "Password $number"
                    this.user = user
                    this.authenticationType = "Auth $number"
                    this.twoFASecretKeys = "Keys $number"
                    this.additionalInfo = "More Info $number"
                }
            )
        }
    }

    fun getUser(number: Int) = User(
        name = "User $number",
        photo = "Photo $number",
        email = "www.useremail$number.com"
    )

    fun getCard(number: Int, user: String) = Card()
        .apply {
            this.entryName = "Card $number"
            this.number = "Number $number"
            this.pin = "Pin $number"
            this.bank = "Bank $number"
            this.expiryDate = "Expiry $number"
            this.issuedDate = "Issued $number"
            this.cardHolderName = "Cardholder $number"
            this.user = user
            this.additionalInfo = "More Info $number"
        }

    fun createCards(size: Int, user: String) = ArrayList<Card>().apply {
        for (number in 1..size) {
            add(
                Card().apply {
                    this.entryName = "Card $number"
                    this.number = "Number $number"
                    this.pin = "Pin $number"
                    this.bank = "Bank $number"
                    this.expiryDate = "Expiry $number"
                    this.issuedDate = "Issued $number"
                    this.cardHolderName = "Cardholder $number"
                    this.user = user
                    this.additionalInfo = "More Info $number"
                }
            )
        }
    }

    fun getBankAccount(number: Int, user: String) =
        BankAccount().apply {
            this.entryName = "Bank Account $number"
            this.accountNumber = "Bank Number $number"
            this.accountOwner = "Account Owner $number"
            this.bank = "Bank $number"
            this.iban = "Iban $number"
            this.swiftCode = "Swift $number"
            this.accent = "Accent $number"
            this.user = user
            this.additionalInfo = "More Info $number"
        }

    fun createBankAccounts(size: Int, user: String) = ArrayList<BankAccount>().apply {
        for (number in 1..size) {
            add(
                BankAccount().apply {
                    this.entryName = "Bank Account $number"
                    this.accountNumber = "Bank Number $number"
                    this.accountOwner = "Account Owner $number"
                    this.bank = "Bank $number"
                    this.iban = "Iban $number"
                    this.swiftCode = "Swift $number"
                    this.accent = "Accent $number"
                    this.user = user
                    this.additionalInfo = "More Info $number"
                }
            )
        }
    }
}