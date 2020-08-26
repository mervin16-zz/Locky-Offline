package com.th3pl4gu3.locky_offline.ui.main.main.password_generator

import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.VALUE_DASH
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.VALUE_EMPTY
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.VALUE_LETTERS_CAPITAL
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.VALUE_LETTERS_LOWER
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.VALUE_NUMBERS
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.VALUE_SPECIALS

/*
* This is the Password Generator object
* It generates password for user "tailor-made" as per the user
* If all options are chosen, the password's length is:
* - 4 lowercase & 4 uppercase
* - 2 numbers
* - 1 Dash
* - 1 Special Character
* Which results in 11 characters
* Here is a list of criteria to generate a password:
* At least one criteria should be chosen before generating a password.
* The password's length should be greater than 3
*/
object PasswordGenerator {

    /* Define all possible options */
    private var hasLowercase = false
    private var hasUppercase = false
    private var hasNumbers = false
    private var hasDash = false
    private var hasSpecialCharacters = false

    /* Define character pools */
    private const val caps = VALUE_LETTERS_CAPITAL
    private const val lowers = VALUE_LETTERS_LOWER
    private const val numbers = VALUE_NUMBERS
    private const val dash = VALUE_DASH
    private const val specials = VALUE_SPECIALS

    /* Define limits */
    private const val caps_limit = 4
    private const val lowers_limit = 4
    private const val numbers_limit = 2
    private const val specials_limit = 1

    /* Always call this function first to set options */
    fun withOptions(
        hasLowercase: Boolean = false,
        hasUppercase: Boolean = false,
        hasNumbers: Boolean = false,
        hasDash: Boolean = false,
        hasSpecialCharacters: Boolean = false,
    ) {
        this.hasLowercase = hasLowercase
        this.hasUppercase = hasUppercase
        this.hasNumbers = hasNumbers
        this.hasDash = hasDash
        this.hasSpecialCharacters = hasSpecialCharacters
    }

    fun generate(): String {
        val password = ArrayList<Char>()

        if (hasLowercase) password.addRandomizedCharacters(lowers, lowers_limit)

        if (hasUppercase) password.addRandomizedCharacters(caps, caps_limit)

        if (hasNumbers) password.addRandomizedCharacters(numbers, numbers_limit)

        if (hasDash) password.add(dash)

        if (hasSpecialCharacters) password.addRandomizedCharacters(specials, specials_limit)

        return password
            .shuffled()
            .joinToString(VALUE_EMPTY)
    }

    private fun ArrayList<Char>.addRandomizedCharacters(pool: String, limit: Int) {
        this.addAll(
            (1..limit)
                .map { kotlin.random.Random.nextInt(0, pool.length) }
                .map(pool::get)
        )
    }
}