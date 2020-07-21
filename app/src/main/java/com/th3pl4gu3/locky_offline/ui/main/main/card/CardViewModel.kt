package com.th3pl4gu3.locky_offline.ui.main.main.card

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.th3pl4gu3.locky_offline.core.main.credentials.Card
import com.th3pl4gu3.locky_offline.core.main.tuning.CardSort
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.CardRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_CARDS_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.getCardType
import java.util.*

class CardViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Live Data Variables
     **/
    private var _loadingStatus = MutableLiveData(Loading.List.LOADING)
    private var _currentCardsExposed =
        CardRepository.getInstance(getApplication()).getAll(activeUser.email)
    private var _sort = MutableLiveData(loadSortObject())

    /**
     * Properties
     **/
    val loadingStatus: LiveData<Loading.List>
        get() = _loadingStatus

    /**
     * Live Data Transformations
     **/
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


    /*
     * Accessible Functions
     */
    /* Flag to stop showing the loading animation */
    internal fun doneLoading(size: Int) {
        _loadingStatus.value = if (size > 0) Loading.List.LIST else Loading.List.EMPTY_VIEW
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


    /*
    * In-accessible functions
    */
    /*
    * Checks if sorting session exists
    * If sessions exists, we return the sort object
    * Else we return a new sort object
    */
    private fun loadSortObject(): CardSort = with(LocalStorageManager) {
        withLogin(getApplication())
        return if (exists(KEY_CARDS_SORT)) {
            get(KEY_CARDS_SORT)!!
        } else CardSort()
    }

    /* Save sort data to Session for persistent re-usability*/
    private fun saveSortToSession(sort: CardSort) = with(LocalStorageManager) {
        withLogin(getApplication())
        put(KEY_CARDS_SORT, sort)
    }
}