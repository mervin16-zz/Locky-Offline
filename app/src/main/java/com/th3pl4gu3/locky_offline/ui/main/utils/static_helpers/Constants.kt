package com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers

object Constants {

    /*
        ****    Settings    ****
     */
    const val SETTINGS_CRYPTO_DIGEST_SCHEME = "SHA-256"

    /*
        ****    Values    ****
     */
    const val VALUE_EMPTY = ""
    const val VALUE_LETTERS_CAPITAL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    const val VALUE_LETTERS_LOWER = "abcdefghijklmnopqrstuvwxyz"
    const val VALUE_NUMBERS = "0123456789"
    const val VALUE_DASH = '-'
    const val VALUE_SPECIALS = "!@#$%^*()+=?/|`~"

    /*
        ****    Key & Value Pairs    ****
     */
    const val KEY_ACCOUNT_LOGO = "KEY_ACCOUNT_LOGO"
    const val KEY_BANK_ACCOUNT_LOGO_HEX = "KEY_BANK_ACCOUNT_LOGO_HEX"
    const val KEY_DEVICE_LOGO_HEX = "KEY_DEVICE_LOGO_HEX"
    const val KEY_DEVICE_LOGO_ICON = "KEY_DEVICE_LOGO_ICON"
    const val KEY_CARDS_SORT = "KEY_CARD_SORT"
    const val KEY_ACCOUNTS_SORT = "KEY_ACCOUNT_SORT"
    const val KEY_BANK_ACCOUNTS_SORT = "KEY_BANK_ACCOUNT_SORT"
    const val KEY_DEVICE_SORT = "KEY_DEVICE_SORT"
    const val KEY_USER_ACCOUNT = "KEY_USER_ACCOUNT"
    const val KEY_CREDENTIAL_RESTORE = "KEY_CREDENTIAL_RESTORE"

    /*
        ****    Regexes    ****
     */
    const val REGEX_CREDIT_CARD_VISA = "^4[0-9]{12}(?:[0-9]{3})?\$"
    const val REGEX_CREDIT_CARD_MASTERCARD =
        "^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}\$"
    const val REGEX_CREDIT_CARD_AMEX = "^3[47]\\d{13,14}\$"
    const val REGEX_CREDIT_CARD_DISCOVER =
        "^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})\$"
    const val REGEX_CREDIT_CARD_JCB = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}\$"
    const val REGEX_CREDIT_CARD_DINNERSCLUB = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}\$"

}