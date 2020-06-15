package com.th3pl4gu3.locky_offline.core.main

import android.app.Application
import android.util.Patterns
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.ui.main.utils.toFormattedCalendarForCard
import java.util.*
import kotlin.collections.HashMap


/*
* Handles form validation across Locky
* All forms should handle validation through this class
*/
class Validation(val application: Application) {
    enum class ErrorField { NAME, PASSWORD, EMAIL, NUMBER, PIN, BANK, OWNER, ISSUED_DATE, EXPIRY_DATE }

    var errorList = HashMap<ErrorField, String>()
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
            isCardDateValid(
                card.issuedDate.toFormattedCalendarForCard(),
                card.expiryDate.toFormattedCalendarForCard()
            )
        }

        return errorList.isEmpty()
    }

    fun isCardDateValid(issuedDate: Calendar, expiryDate: Calendar): Boolean {
        if (issuedDate.after(expiryDate)) {
            errorList[ErrorField.ISSUED_DATE] = "Issued date cannot be greater than expiry date"
        }

        if (expiryDate.before(issuedDate)) {
            errorList[ErrorField.EXPIRY_DATE] = "Expiry date cannot be less than issued date."
        }

        return errorList.isEmpty()
    }

    private fun emptyValueCheck(data: String, field: ErrorField) {
        if (data.isEmpty()) errorList[field] =
            application.getString(R.string.error_field_validation_blank)
    }

    private fun validateEmail(email: String) {
        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) errorList[ErrorField.EMAIL] =
            application.getString(R.string.error_field_validation_email_format)
    }
}