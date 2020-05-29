package com.th3pl4gu3.locky_offline.core.main

import android.util.Patterns
import com.th3pl4gu3.locky_offline.core.Constants.Companion.ERROR_MESSAGE_EMAIL_FORMAT
import com.th3pl4gu3.locky_offline.core.Constants.Companion.ERROR_MESSAGE_FIELD_CANNOT_BE_EMPTY
import com.th3pl4gu3.locky_offline.core.Constants.Companion.EXCEPTION_FORM
import com.th3pl4gu3.locky_offline.core.exceptions.FormException

class Validation() {
    enum class ErrorField { NAME, USERNAME, EMAIL, PASSWORD, NUMBER, PIN, BANK, CARD_HOLDER, }

    var errorList = HashMap<ErrorField, String>()
        private set

    @Throws(Exception::class)
    fun validateAccountForm(account: Account) {
        with(account) {
            emptyValueCheck(
                accountName,
                ErrorField.NAME
            )
            emptyValueCheck(
                username,
                ErrorField.USERNAME
            )
            emptyValueCheck(
                email,
                ErrorField.EMAIL
            )
            validateEmail(email)
            emptyValueCheck(
                password,
                ErrorField.PASSWORD
            )
        }

        if (errorList.isNotEmpty()) throw FormException(EXCEPTION_FORM)
    }

    @Throws(Exception::class)
    fun validateCardForm(card: Card) {
        with(card) {
            emptyValueCheck(
                entryName,
                ErrorField.NAME
            )
            emptyValueCheck(
                number.toString(),
                ErrorField.NUMBER
            )
            emptyValueCheck(
                pin.toString(),
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

        if (errorList.isNotEmpty()) throw FormException(EXCEPTION_FORM)
    }

    private fun emptyValueCheck(data: String, field: ErrorField) {
        if (data.isEmpty()) errorList[field] = ERROR_MESSAGE_FIELD_CANNOT_BE_EMPTY
    }

    private fun validateEmail(email: String) {
        if (email.isEmpty()) errorList[ErrorField.EMAIL] = ERROR_MESSAGE_FIELD_CANNOT_BE_EMPTY

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) errorList[ErrorField.EMAIL] =
            ERROR_MESSAGE_EMAIL_FORMAT
    }
}