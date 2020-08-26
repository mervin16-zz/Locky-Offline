package com.th3pl4gu3.locky_offline.ui.main.main.card

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.toLiveData
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.core.tuning.CardSort
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.CardRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.getCardType
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.resources
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_CARDS_SORT
import kotlinx.coroutines.launch
import java.util.*

class CardViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Live Data Variables
     **/
    private var _loadingStatus = MutableLiveData(Loading.List.LOADING)
    private var _cards =
        CardRepository.getInstance(getApplication()).getAll(activeUser.email)
    private var _sort = MutableLiveData(loadSortObject())

    /**
     * Properties
     **/
    val loadingStatus: LiveData<Loading.List>
        get() = _loadingStatus

    private val DataSource.Factory<Int, Card>.sortByName: DataSource.Factory<Int, Card>
        get() {
            return this@sortByName.mapByPage { list ->
                list.sortedBy {
                    it.entryName.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    private val DataSource.Factory<Int, Card>.sortByType: DataSource.Factory<Int, Card>
        get() {
            return this@sortByType.mapByPage { list ->
                list.sortedBy { card -> card.number.getCardType() }
            }
        }

    private val DataSource.Factory<Int, Card>.sortByBank: DataSource.Factory<Int, Card>
        get() {
            return this@sortByBank.mapByPage { list ->
                list.sortedBy { card ->
                    card.bank.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    private val DataSource.Factory<Int, Card>.sortedByCardHolderName: DataSource.Factory<Int, Card>
        get() {
            return this@sortedByCardHolderName.mapByPage { list ->
                list.sortedBy { card ->
                    card.cardHolderName.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    /**
     * Live Data Transformations
     **/
    val cards = Transformations.switchMap(_sort) {
        when (true) {
            it.sortByName -> _cards.sortByName
            it.sortByType -> _cards.sortByType
            it.sortByBank -> _cards.sortByBank
            it.sortByCardHolderName -> _cards.sortedByCardHolderName
            else -> _cards
        }.toLiveData(pageSize = resources.getInteger(R.integer.size_paging_list_default))
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

    internal fun add(card: Card) =
        viewModelScope.launch { CardRepository.getInstance(getApplication()).insert(card) }

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