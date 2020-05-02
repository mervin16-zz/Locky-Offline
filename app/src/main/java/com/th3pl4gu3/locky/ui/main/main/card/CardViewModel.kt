package com.th3pl4gu3.locky.ui.main.main.card

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.core.tuning.CardSort
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.repository.database.CardDao
import com.th3pl4gu3.locky.ui.main.utils.Constants
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky.ui.main.utils.getCardType
import java.util.*
import kotlin.collections.ArrayList

class CardViewModel(application: Application) : AndroidViewModel(application) {

    private val _showSnackbarEvent = MutableLiveData<String>()
    private val _loadingStatus = MutableLiveData<LoadingStatus>()

    private val _cardSnapShotList = CardDao().getAll()
    private var _currentCardsExposed = MediatorLiveData<List<Card>>()

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

    private var _sort = MutableLiveData<CardSort>()

    init {
        //Load the cards for the first time
        loadCards()
        //Set the default value for card sort
        _sort.value = checkSorting()
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

    val showSnackBarEvent: LiveData<String>
        get() = _showSnackbarEvent

    val loadingStatus: LiveData<LoadingStatus>
        get() = _loadingStatus

    internal fun setSnackBarMessage(message: String) {
        _showSnackbarEvent.value = message
    }

    internal fun doneShowingSnackBar() {
        _showSnackbarEvent.value = null
    }

    internal fun setLoading(status: LoadingStatus) {
        _loadingStatus.value = status
    }


    internal fun refreshSort(sort: CardSort) {
        _sort.value = sort
    }

    private fun loadCards() {
        _currentCardsExposed.addSource(_cardSnapShotList) { snapshot ->
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
                postSnapshot.getValue<Card>()?.let { cardList.add(it) }
            }
            cardList
        } else {
            ArrayList()
        }
}