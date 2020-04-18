package com.th3pl4gu3.locky.core

import android.util.Patterns
import com.th3pl4gu3.locky.core.exceptions.FormException
import java.lang.Exception

class Validation(private val credentials: Credentials) {
    enum class ErrorField { NAME, USERNAME, EMAIL, PASSWORD, NUMBER, PIN, BANK, CARD_HOLDER, }

    var errorList = HashMap<ErrorField, String>()
        private set

    @Throws(Exception::class)
    fun validateAccountForm(){
        val account = credentials as Account
        emptyValueCheck(account.name, ErrorField.NAME)
        emptyValueCheck(account.username, ErrorField.USERNAME)
        emptyValueCheck(account.email, ErrorField.EMAIL)
        validateEmail(account.email)
        emptyValueCheck(account.password, ErrorField.PASSWORD)

        if(errorList.isNotEmpty()) throw FormException("Field errors.")
    }

    @Throws(Exception::class)
    fun validateCardForm(){
        val card = credentials as Card
        emptyValueCheck(card.name, ErrorField.NAME)
        emptyValueCheck(card.number.toString(), ErrorField.NUMBER)
        emptyValueCheck(card.pin.toString(), ErrorField.PIN)
        emptyValueCheck(card.bank, ErrorField.BANK)
        emptyValueCheck(card.cardHolderName, ErrorField.CARD_HOLDER)
        if(errorList.isNotEmpty()) throw FormException("Field errors.")
    }

    private fun emptyValueCheck(data: String, field: ErrorField) {
        if (data.isEmpty()) errorList[field] = "This field cannot be empty."
    }

    private fun validateEmail(email: String){
        if(email.isEmpty()) errorList[ErrorField.EMAIL] = "This field cannot be empty."

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) errorList[ErrorField.EMAIL] = "Wrong email format."
    }
}