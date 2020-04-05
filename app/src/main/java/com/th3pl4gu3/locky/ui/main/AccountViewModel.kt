package com.th3pl4gu3.locky.ui.main

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
        val account1 = Account("1")
        account1.name = "Facebook"
        account1.username = "mervin16"
        account1.email = "mervin@gmail.com"
        account1.website = "facebook.com"

        val account2 = Account("2")
        account2.name = "Google"
        account2.username = "mervin16"
        account2.email = "mervin@gmail.com"
        account2.website = "google.com"

        val account3 = Account("3")
        account3.name = "Youtube"
        account3.username = "mervin16"
        account3.website = "youtube.com"

        val account4 = Account("4")
        account4.name = "Android"
        account4.email = "mervin@gmail.com"
        account4.website = "android.com"

        val account5 = Account("5")
        account5.name = "Apple"
        account5.email = "mervin@gmail.com"
        account5.website = "apple.com"

        val account6 = Account("6")
        account6.name = "Spotify"
        account6.username = "mervin16"
        account6.website = "spotify.com"

        val account7 = Account("7")
        account7.name = "MCB"
        account7.username = "mervin16"
        account7.website = "mcb.mu"

        val account8 = Account("8")
        account8.name = "Tinder"
        account8.username = "mervin16"
        account8.website = "tinder.com"

        val account9 = Account("9")
        account9.name = "Udacity"
        account9.username = "mervin16"
        account9.website = "udacity.com"

        val account10 = Account("10")
        account10.name = "udemy"
        account10.email = "mervin@gmail.com"
        account10.website = "udemy.com"

        val account11 = Account("11")
        account11.name = "Whatsapp"
        account11.username = "mervin16"
        account11.website = "whatsappbrand.com"

        val account12 = Account("12")
        account12.name = "netflix"
        account12.username = "mervin16"
        account12.website = "netflixinvestor.com"

        val accounts = java.util.ArrayList<Account>()
        accounts.add(account1)
        accounts.add(account2)
        accounts.add(account3)
        accounts.add(account4)
        accounts.add(account5)
        accounts.add(account6)
        accounts.add(account7)
        accounts.add(account8)
        accounts.add(account9)
        accounts.add(account10)
        accounts.add(account11)
        accounts.add(account12)

        return accounts
    }
}