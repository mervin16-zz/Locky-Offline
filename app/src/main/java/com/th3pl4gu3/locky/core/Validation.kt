package com.th3pl4gu3.locky.core

import android.util.Patterns
import com.th3pl4gu3.locky.core.exceptions.FormException
import java.lang.Exception

class Validation(private val credentials: Credentials) {
    enum class ErrorField {NAME, USERNAME, EMAIL, PASSWORD}

    var errorList = HashMap<ErrorField, String>()
        private set

    @Throws(Exception::class)
    fun validateAccountForm(){
        val account = credentials as Account
        validateName(account.name)
        validateUsername(account.username)
        validateEmail(account.email)
        validatePassword(account.password)

        if(errorList.isNotEmpty()) throw FormException("Field errors.")
    }

    @Throws(Exception::class)
    fun validateCardForm(){
        val card = credentials as Card
        //TODO: Implement card validation method
        if(errorList.isNotEmpty()) throw FormException("Field errors.")
    }

    private fun validateName(name: String){
        if(name.isEmpty()) errorList[ErrorField.NAME] = "This field cannot be empty."
    }

    private fun validateUsername(username: String){
        if(username.isEmpty()) errorList[ErrorField.USERNAME] = "This field cannot be empty."
    }

    private fun validateEmail(email: String){
        if(email.isEmpty()) errorList[ErrorField.EMAIL] = "This field cannot be empty."

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) errorList[ErrorField.EMAIL] = "Wrong email format."
    }

    private fun validatePassword(password: String){
        if(password.isEmpty()) errorList[ErrorField.PASSWORD] = "This field cannot be empty."

        if(password.length < 6) errorList[ErrorField.PASSWORD] = "Password length should be at least 6 characters."
    }
}