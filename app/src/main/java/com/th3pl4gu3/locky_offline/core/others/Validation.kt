package com.th3pl4gu3.locky_offline.core.others

import android.app.Application
import android.util.Patterns
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Account
import com.th3pl4gu3.locky_offline.core.credentials.BankAccount
import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.core.credentials.Device
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toFormattedCalendarForCard
import java.util.*
import kotlin.collections.HashMap


/*
* Handles form validation across Locky
* All forms should handle validation through this class
*/
class Validation(val application: Application) {

    enum class ErrorField { NAME, PASSWORD, CONFIRM_PASSWORD, CURRENT_PASSWORD, EMAIL, CVC, USERNAME, NUMBER, PIN, BANK, OWNER, ISSUED_DATE, EXPIRY_DATE }

    var errorList = HashMap<ErrorField, String>()
        private set

    fun isNewMaterPasswordValid(
        savedCurrent: String?,
        enteredCurrent: String,
        pass: String,
        confirm: String
    ): Boolean {
        emptyValueCheck(
            enteredCurrent,
            ErrorField.CURRENT_PASSWORD
        )

        emptyValueCheck(
            pass,
            ErrorField.PASSWORD
        )

        emptyValueCheck(
            confirm,
            ErrorField.CONFIRM_PASSWORD
        )

        if (savedCurrent != enteredCurrent) {
            errorList[ErrorField.CURRENT_PASSWORD] =
                application.getString(R.string.error_field_validation_password_notsame)
        }

        if (pass.length <= 6) {
            errorList[ErrorField.PASSWORD] =
                application.getString(R.string.error_field_validation_password_criteria)
        }

        if (pass != confirm) {
            errorList[ErrorField.CONFIRM_PASSWORD] =
                application.getString(R.string.error_field_validation_password_notmatch)
        }

        return errorList.isEmpty()
    }

    fun isMaterPasswordValid(pass: String, confirm: String): Boolean {
        emptyValueCheck(
            pass,
            ErrorField.PASSWORD
        )

        emptyValueCheck(
            confirm,
            ErrorField.CONFIRM_PASSWORD
        )

        if (pass.length <= 6) {
            errorList[ErrorField.PASSWORD] =
                application.getString(R.string.error_field_validation_password_criteria)
        }

        if (pass != confirm) {
            errorList[ErrorField.CONFIRM_PASSWORD] =
                application.getString(R.string.error_field_validation_password_notmatch)
        }

        return errorList.isEmpty()
    }

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
                cvc,
                ErrorField.CVC
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
            validateCvc(cvc)
        }

        return errorList.isEmpty()
    }

    fun isDeviceFormValid(device: Device): Boolean {
        with(device) {
            emptyValueCheck(
                entryName,
                ErrorField.NAME
            )
            emptyValueCheck(
                username,
                ErrorField.USERNAME
            )
            emptyValueCheck(
                password,
                ErrorField.PASSWORD
            )
        }

        return errorList.isEmpty()
    }

    fun isCardDateValid(issuedDate: Calendar, expiryDate: Calendar): Boolean {
        if (issuedDate.after(expiryDate)) {
            errorList[ErrorField.ISSUED_DATE] =
                application.getString(R.string.error_field_validation_id_after)
        }

        if (expiryDate.before(issuedDate)) {
            errorList[ErrorField.EXPIRY_DATE] =
                application.getString(R.string.error_field_validation_ed_before)
        }

        return errorList.isEmpty()
    }

    private fun emptyValueCheck(data: String, field: ErrorField) {
        if (data.isEmpty()) errorList[field] =
            application.getString(R.string.error_field_validation_blank)
    }

    private fun validateCvc(data: String) {
        if (data.isNotEmpty() && data.length != 3) errorList[ErrorField.CVC] =
            application.getString(R.string.error_field_validation_cvc_format)
    }

    private fun validateEmail(email: String) {
        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) errorList[ErrorField.EMAIL] =
            application.getString(R.string.error_field_validation_email_format)
    }
}