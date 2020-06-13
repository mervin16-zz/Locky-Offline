package com.th3pl4gu3.locky_offline.core.main

import android.util.Patterns

class Validation {
    enum class ErrorField { NAME, PASSWORD, EMAIL, NUMBER, PIN, BANK, OWNER, }
    enum class ErrorType { BLANK_FIELD, EMAIL_FORMAT }

    var errorList = HashMap<ErrorField, ErrorType>()
        private set

    fun isBankAccountFormValid(account: BankAccount): Boolean {
        with(account) {
            emptyValueCheck(
                entryName,
                ErrorField.NAME
            )
            emptyValueCheck(
                accountNumber,
                ErrorField.NUMBER
            )
            emptyValueCheck(
                bank,
                ErrorField.BANK
            )
            emptyValueCheck(
                accountOwner,
                ErrorField.OWNER
            )
        }

        return errorList.isEmpty()
    }

    fun isAccountFormValid(account: Account): Boolean {
        with(account) {
            emptyValueCheck(
                entryName,
                ErrorField.NAME
            )
            emptyValueCheck(
                password,
                ErrorField.PASSWORD
            )
            validateEmail(
                email
            )
        }

        return errorList.isEmpty()
    }

    fun isCardFormValid(card: Card): Boolean {
        with(card) {
            emptyValueCheck(
                entryName,
                ErrorField.NAME
            )
            emptyValueCheck(
                number,
                ErrorField.NUMBER
            )
            emptyValueCheck(
                pin,
                ErrorField.PIN
            )
            emptyValueCheck(
                bank,
                ErrorField.BANK
            )
            emptyValueCheck(
                cardHolderName,
                ErrorField.OWNER
            )
        }

        return errorList.isEmpty()
    }

    private fun emptyValueCheck(data: String, field: ErrorField) {
        if (data.isEmpty()) errorList[field] = ErrorType.BLANK_FIELD
    }

    private fun validateEmail(email: String) {
        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) errorList[ErrorField.EMAIL] =
            ErrorType.EMAIL_FORMAT
    }
}