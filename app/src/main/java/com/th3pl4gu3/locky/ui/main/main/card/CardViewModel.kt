package com.th3pl4gu3.locky.ui.main.main.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.th3pl4gu3.locky.core.Card

class CardViewModel : ViewModel(){

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

    internal fun generateDummyCards(): ArrayList<Card>{
        val card1 = Card(
            "1",
            name = "Mervin's Account",
            number = 4850508089006089,
            bank = "MCB",
            pin = 1234,
            cardHolderName = "Mervin Hemaraju",
            additionalInfo = "Information"
        )
        val card2 = Card(
            "1",
            name = "Mervin's Account",
            number = 5150508089006089,
            bank = "MCB",
            pin = 1234,
            cardHolderName = "Mervin Hemaraju",
            additionalInfo = "Information"
        )
        val card3 = Card(
            "1",
            name = "Mervin's Account",
            number = 5450508089006089,
            bank = "MCB",
            pin = 1234,
            cardHolderName = "Mervin Hemaraju",
            additionalInfo = "Information"
        )
        val card4 = Card(
            "1",
            name = "Mervin's Account",
            number = 3550508089006089,
            bank = "MCB",
            pin = 1234,
            cardHolderName = "Mervin Hemaraju",
            additionalInfo = "Information"
        )
        val card5 = Card(
            "1",
            name = "Mervin's Account",
            number = 3000508089006089,
            bank = "MCB",
            pin = 1234,
            cardHolderName = "Mervin Hemaraju"
        )
        val card6 = Card(
            "1",
            name = "Mervin's Account",
            number = 6011508089006089,
            bank = "MCB",
            pin = 1234,
            cardHolderName = "Mervin Hemaraju",
            additionalInfo = "Information"
        )
        val card7 = Card(
            "1",
            name = "Mervin's Account",
            number = 1800508089006089,
            bank = "MCB",
            pin = 1234,
            cardHolderName = "Mervin Hemaraju",
            additionalInfo = "Information"
        )
        val card8 = Card(
            "1",
            name = "Mervin's Account",
            number = 2130508089006089,
            bank = "MCB",
            pin = 1234,
            cardHolderName = "Mervin Hemaraju",
            additionalInfo = "Information"
        )

        return java.util.ArrayList<Card>().apply {
            add(card1)
            add(card2)
            add(card3)
            add(card4)
            add(card5)
            add(card6)
            add(card7)
            add(card8)
        }
    }
}