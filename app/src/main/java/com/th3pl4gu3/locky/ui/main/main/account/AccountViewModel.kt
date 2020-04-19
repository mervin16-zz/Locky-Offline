package com.th3pl4gu3.locky.ui.main.main.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.th3pl4gu3.locky.core.Account

class AccountViewModel : ViewModel(){

    /**
     * Request a toast by setting this value to true.
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private var _showSnackbarEvent = MutableLiveData<String>()

    /**
     * If this is true, immediately `show()` a toast and call `doneShowingSnackbar()`.
     */
    val showSnackBarEvent: LiveData<String>
        get() = _showSnackbarEvent

    fun setSnackBarMessage(message: String) {
        _showSnackbarEvent.value = message
    }

    /**
     * Call this immediately after calling `show()` on a toast.
     *
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }

    internal fun generateDummyAccounts(): ArrayList<Account>{
        val account1 = Account(
            "1",
            name = "Facebook",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            logoUrl = "https://logo.clearbit.com/facebook.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account2 = Account(
            "1",
            name = "Google",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/google.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account3 = Account(
            "1",
            name = "Instagram",
            username = "mervin16",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/instagram.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account4 = Account(
            "1",
            name = "Spotify",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/spotify.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account5 = Account(
            "1",
            name = "Netflix",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/netflixinvestor.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account6 = Account(
            "1",
            name = "Udemy",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/udemy.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account7 = Account(
            "1",
            name = "Android",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/android.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account8 = Account(
            "1",
            name = "Apple",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/apple.com",
            additionalInfo = "Information"
        )
        val account9 = Account(
            "1",
            name = "LG",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/lg.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account10 = Account(
            "1",
            name = "Huawei",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/huawei.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account11 = Account(
            "1",
            name = "Sony",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/sony.net",
            additionalInfo = "Information"
        )
        val account12 = Account(
            "1",
            name = "MCB",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/mcb.mu",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account13 = Account(
            "1",
            name = "Microsoft",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/microsoft.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account14 = Account(
            "1",
            name = "Github",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/github.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )
        val account15 = Account(
            "1",
            name = "Air Mauritius",
            username = "mervin16",
            email = "mervin@gmail.com",
            password = "thisisapassword",
            website = "www.domain.com",
            logoUrl = "https://logo.clearbit.com/airmauritius.com",
            additionalInfo = "Information",
            twoFA = "No",
            twoFASecretKeys = "None"
        )

        return ArrayList<Account>().apply {
            add(account1)
            add(account2)
            add(account3)
            add(account4)
            add(account5)
            add(account6)
            add(account7)
            add(account8)
            add(account9)
            add(account10)
            add(account11)
            add(account12)
            add(account13)
            add(account14)
            add(account15)
        }
    }
}