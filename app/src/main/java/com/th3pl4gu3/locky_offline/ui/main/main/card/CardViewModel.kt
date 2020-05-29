package com.th3pl4gu3.locky_offline.ui.main.main.card

import android.app.Application
import androidx.lifecycle.*
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.core.main.CardSort
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.repository.LoadingStatus
import com.th3pl4gu3.locky_offline.repository.database.CardRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.KEY_CARDS_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.getCardType
import java.util.*

class CardViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Live Data Variables
     **/
    private val _showSnackBarEvent = MutableLiveData<String>()
    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    private var _currentCardsExposed = MediatorLiveData<List<Card>>()
    private var _sort = MutableLiveData(loadSortObject())
    private var _cardListVisibility = MutableLiveData(false)
    private var _cardEmptyViewVisibility = MutableLiveData(false)

    /**
     * Init Clause
     **/
    init {
        /*
        * Here, we define all codes that need to
        * run on startup.
        */

        /* We show loading animation */
        _loadingStatus.value = LoadingStatus.LOADING

        /* We load the cards */
        loadCards()
    }

    /**
     * Live Data Transformations
     **/
    val loadingStatus: LiveData<Boolean> = Transformations.map(_loadingStatus) {
        it == LoadingStatus.LOADING
    }

    private val sortedByName = Transformations.map(_currentCardsExposed) {
        it.sortedBy { card ->
            card.entryName.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByType =
        Transformations.map(_currentCardsExposed) { it.sortedBy { card -> card.number.getCardType() } }

    private val sortedByBank = Transformations.map(_currentCardsExposed) {
        it.sortedBy { card ->
            card.bank.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByCardHolderName = Transformations.map(_currentCardsExposed) {
        it.sortedBy { card ->
            card.cardHolderName.toLowerCase(
                Locale.ROOT
            )
        }
    }

    val cards: LiveData<List<Card>> = Transformations.switchMap(_sort) {
        when (true) {
            it.sortByName -> sortedByName
            it.sortByType -> sortedByType
            it.sortByBank -> sortedByBank
            it.sortByCardHolderName -> sortedByCardHolderName
            else -> _currentCardsExposed
        }
    }


    /**
     * Properties
     **/
    val showSnackBarEvent: LiveData<String>
        get() = _showSnackBarEvent

    val cardListVisibility: LiveData<Boolean>
        get() = _cardListVisibility

    val cardEmptyViewVisibility: LiveData<Boolean>
        get() = _cardEmptyViewVisibility


    /**
     * Functions
     **/

    /* Flag to show snack bar message*/
    internal fun setSnackBarMessage(message: String) {
        _showSnackBarEvent.value = message
    }

    /* Flag to stop showing snack bar message */
    internal fun doneShowingSnackBar() {
        _showSnackBarEvent.value = null
    }

    /* Flag to stop showing the loading animation */
    internal fun doneLoading() {
        _loadingStatus.value = LoadingStatus.DONE
    }

    /* Alternates the visibility between account list and empty view UI */
    internal fun alternateCardListVisibility(cardsSize: Int) {
        _cardListVisibility.value = cardsSize > 0
        _cardEmptyViewVisibility.value = cardsSize < 1
    }

    /* Call function whenever there is a change in sorting */
    internal fun sortChange(sort: CardSort) {
        /*
        * We first save the sort to session
        * Then we change the value of sort
        */
        if (_sort.value.toString() != sort.toString()) {
            saveSortToSession(sort)
            _sort.value = sort
        }
    }

    /* Load the card into a mediator live data */
    private fun loadCards() {
        _currentCardsExposed.addSource(
            CardRepository.getInstance(getApplication()).getAll(getUser().email)
        ) {
            _currentCardsExposed.value = it
        }
    }

    /*
    * Checks if sorting session exists
    * If sessions exists, we return the sort object
    * Else we return a new sort object
    */
    private fun loadSortObject(): CardSort {
        LocalStorageManager.with(getApplication())
        return if (LocalStorageManager.exists(KEY_CARDS_SORT)) {
            LocalStorageManager.get(KEY_CARDS_SORT)!!
        } else CardSort()
    }

    /* Save sort data to Session for persistent re-usability*/
    private fun saveSortToSession(sort: CardSort) {
        LocalStorageManager.with(getApplication())
        LocalStorageManager.put(KEY_CARDS_SORT, sort)
    }

    private fun getUser(): User {
        LocalStorageManager.with(getApplication())
        return LocalStorageManager.get<User>(Constants.KEY_USER_ACCOUNT)!!
    }
}