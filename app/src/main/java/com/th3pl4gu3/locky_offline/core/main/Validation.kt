package com.th3pl4gu3.locky_offline.core.main

class Validation {
    enum class ErrorField { NAME, PASSWORD, NUMBER, PIN, BANK, CARD_HOLDER, }
    enum class ErrorType { BLANK_FIELD }

    var errorList = HashMap<ErrorField, ErrorType>()
        private set

    fun isAccountFormValid(account: Account): Boolean {
        with(account) {
            emptyValueCheck(
                accountName,
                ErrorField.NAME
            )
            emptyValueCheck(
                password,
                ErrorField.PASSWORD
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
                ErrorField.CARD_HOLDER
            )
        }

        return errorList.isEmpty()
    }

    private fun emptyValueCheck(data: String, field: ErrorField) {
        if (data.isEmpty()) errorList[field] = ErrorType.BLANK_FIELD
    }
}