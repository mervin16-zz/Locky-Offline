package com.th3pl4gu3.locky.ui.main.main.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.repository.database.CardDao

class CardViewModel : ViewModel() {

    private val _showSnackbarEvent = MutableLiveData<String>()
    private val _loadingStatus = MutableLiveData<LoadingStatus>()
    private val _cardSnapShotList = CardDao().getAll()
    private val _mediatorCards = MediatorLiveData<List<Card>>()

    init {
        //Load the cards for the first time
        loadCards()
    }

    val cards: LiveData<List<Card>>
        get() = _mediatorCards

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

    /*internal fun updateCards(filter: CardFilter) {
        when (true) {
            filter.cardType -> {
            }
            filter.bank -> {



            }
            filter.cardHolderName -> {
                reloadCards(
                    Transformations.map(_mediatorCards) { list ->
                        list.sortedByDescending { card -> card.cardHolderName }
                    }
                )
            }
        }
    }

    private fun reloadCards(cards: LiveData<List<Card>>) {
        _mediatorCards.removeSource(_cardSnapShotList)
        _mediatorCards.addSource(cards) {
            _mediatorCards.value = it
        }
    }*/

    private fun loadCards() {
        _mediatorCards.addSource(_cardSnapShotList) { snapshot ->
            if (snapshot != null) {
                val cardList = ArrayList<Card>()
                snapshot.children.forEach { postSnapshot ->
                    postSnapshot.getValue<Card>()?.let { cardList.add(it) }
                }
                _mediatorCards.value = cardList
            }
        }
    }
}