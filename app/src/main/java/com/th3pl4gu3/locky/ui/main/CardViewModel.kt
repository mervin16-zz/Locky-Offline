package com.th3pl4gu3.locky.ui.main

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
        val card1 = Card("1")
        card1.name = "Mervin's Account"
        card1.number = 4850508089006089

        val card2 = Card("2")
        card2.name = "Mervin's Account"
        card2.number = 5150508089006089

        val card3 = Card("3")
        card3.name = "Mervin's Account"
        card3.number = 5450508089006089

        val card4 = Card("4")
        card4.name = "Mervin's Account"
        card4.number = 3550508089006089

        val card5 = Card("5")
        card5.name = "Mervin's Account"
        card5.number = 3000508089006089

        val card6 = Card("6")
        card6.name = "Mervin's Account"
        card6.number = 6011508089006089

        val card7 = Card("7")
        card7.name = "Mervin's Account"
        card7.number = 2130508089006089

        val card8 = Card("8")
        card8.name = "Mervin's Account"
        card8.number = 1800508089006089

        val cards = java.util.ArrayList<Card>()
        cards.add(card1)
        cards.add(card2)
        cards.add(card3)
        cards.add(card4)
        cards.add(card5)
        cards.add(card6)
        cards.add(card7)
        cards.add(card8)

        return cards
    }
}