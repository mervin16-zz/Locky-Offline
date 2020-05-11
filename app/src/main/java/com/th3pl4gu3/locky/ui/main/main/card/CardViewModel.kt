package com.th3pl4gu3.locky.ui.main.main.card

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.core.main.Card
import com.th3pl4gu3.locky.core.main.CardSort
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.repository.database.CardDao
import com.th3pl4gu3.locky.ui.main.utils.Constants
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky.ui.main.utils.getCardType
import java.util.*
import kotlin.collections.ArrayList

class CardViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Live Data Variables
     **/
    private val _showSnackbarEvent = MutableLiveData<String>()
    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    private var _currentCardsExposed = MediatorLiveData<List<Card>>()
    private var _sort = MutableLiveData<CardSort>()
    private var _cardListVisibility = MutableLiveData(false)
    private var _cardEmptyViewVisibility = MutableLiveData(false)

    /**
     * Init Clause
     **/
    init {
        //Set the loading status
        _loadingStatus.value = LoadingStatus.LOADING
        //Load the cards for the first time
        loadCards()
        //Set the default value for card sort
        _sort.value = checkSorting()
    }

    /**
     * Live Data Transformations
     **/
    private val sortedByName = Transformations.map(_currentCardsExposed) {
        it.sortedBy { card ->
            card.name.toLowerCase(
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

    val cards = Transformations.switchMap(_sort) {
        when (true) {
            it.sortByName -> sortedByName
            it.sortByType -> sortedByType
            it.sortByBank -> sortedByBank
            it.sortByCardHolderName -> sortedByCardHolderName
            else -> _currentCardsExposed
        }
    }

    val loadingStatus: LiveData<Boolean> = Transformations.map(_loadingStatus) {
        when (it) {
            LoadingStatus.LOADING -> {
                true
            }
            LoadingStatus.DONE, LoadingStatus.ERROR -> {
                false
            }
            else -> {
                false
            }
        }
    }

    /**
     * Properties
     **/
    val showSnackBarEvent: LiveData<String>
        get() = _showSnackbarEvent

    val cardListVisibility: LiveData<Boolean>
        get() = _cardListVisibility

    val cardEmptyViewVisibility: LiveData<Boolean>
        get() = _cardEmptyViewVisibility

    /**
     * Functions
     **/
    internal fun alternateCardListVisibility(accountListSize: Int) {
        /**
         * Alternate visibility for card list
         * and empty view for cards
         */
        if (accountListSize > 0) {
            _cardListVisibility.value = true
            _cardEmptyViewVisibility.value = false

            return
        }

        _cardListVisibility.value = false
        _cardEmptyViewVisibility.value = true
    }

    internal fun setSnackBarMessage(message: String) {
        _showSnackbarEvent.value = message
    }

    internal fun doneLoading() {
        _loadingStatus.value = LoadingStatus.DONE
    }

    internal fun doneShowingSnackBar() {
        _showSnackbarEvent.value = null
    }

    internal fun refreshSort(sort: CardSort) {
        _sort.value = sort
    }

    private fun loadCards() {
        _currentCardsExposed.addSource(CardDao().getAll(getUserID())) { snapshot ->
            _currentCardsExposed.value = decomposeDataSnapshots(snapshot)
        }
    }

    private fun checkSorting(): CardSort {
        LocalStorageManager.with(getApplication())
        return if (LocalStorageManager.exists(Constants.KEY_CARDS_SORT)) {
            LocalStorageManager.get(Constants.KEY_CARDS_SORT)!!
        } else CardSort()
    }

    private fun decomposeDataSnapshots(snapshot: DataSnapshot?): List<Card> =
        if (snapshot != null) {
            val cardList = ArrayList<Card>()
            snapshot.children.forEach { postSnapshot ->
                postSnapshot.getValue<Card>()
                    ?.let { cardList.add(it) }
            }
            cardList
        } else {
            ArrayList()
        }

    private fun getUserID(): String {
        LocalStorageManager.with(getApplication())
        return LocalStorageManager.get<User>(Constants.KEY_USER_ACCOUNT)?.id
            ?: TODO("LiveData to catch errors implementation here.")
    }
}