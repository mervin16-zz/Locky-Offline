package com.th3pl4gu3.locky.ui.main.main.search

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.Account
import com.th3pl4gu3.locky.core.main.Card
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.repository.database.AccountDao
import com.th3pl4gu3.locky.repository.database.CardDao
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import java.util.*
import kotlin.collections.ArrayList

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _showSnackbarEvent = MutableLiveData<String>()
    private val _starterScreenVisibility = MutableLiveData(true)
    private val _cards = MediatorLiveData<List<Card>>()
    private val _accounts = MediatorLiveData<List<Account>>()
    private var _searchQuery = ""


    val showSnackBarEvent: LiveData<String>
        get() = _showSnackbarEvent

    val starterScreenVisibility: LiveData<Boolean>
        get() = _starterScreenVisibility

    val accounts: LiveData<List<Account>>
        get() = _accounts

    val cards: LiveData<List<Card>>
        get() = _cards

    val accountsSize: LiveData<String> = Transformations.map(_accounts) {
        getApplication<Application>().getString(
            R.string.text_subtitle_search_accounts,
            it.size.toString()
        )
    }

    val cardsSize: LiveData<String> = Transformations.map(_cards) {
        getApplication<Application>().getString(
            R.string.text_subtitle_search_cards,
            it.size.toString()
        )
    }

    val isAccountListVisible: LiveData<Boolean> = Transformations.map(_accounts) {
        it.isNotEmpty()
    }

    val isCardListVisible: LiveData<Boolean> = Transformations.map(_cards) {
        it.isNotEmpty()
    }

    internal fun setSnackBarMessage(message: String) {
        _showSnackbarEvent.value = message
    }

    internal fun doneShowingSnackBar() {
        _showSnackbarEvent.value = null
    }

    internal fun search(query: String) {
        /* First we check if starter screen is hidden
        * If not hidden, we hide starter screen
        * The live data will automatically make the lists visible
        */
        hideStarterScreen()

        _searchQuery = query.trim().toLowerCase(Locale.ROOT)

        contentLoadingCheck(_searchQuery)
    }

    internal fun cancel() {
        /*If user clicks on close button
        * we check if starter screen is hidden
        * If it is hidden, we show starter screen
        * The live data will automatically make the lists gone
        */
        showStarterScreen()
    }

    private fun showStarterScreen() {
        if (!_starterScreenVisibility.value!!) {
            _starterScreenVisibility.value = true
        }
    }

    private fun hideStarterScreen() {
        if (_starterScreenVisibility.value!!) {
            _starterScreenVisibility.value = false
        }
    }

    private fun contentLoadingCheck(value: String) {
        if (value.isNotEmpty()) {
            loadCards()
            loadAccounts()
        } else {
            _accounts.value = ArrayList()
            _cards.value = ArrayList()
        }
    }

    private fun loadCards() {
        /*
        * Get all cards by the user ID and add to source
        */
        _cards.addSource(CardDao().getAll(getUserID())) { snapshot ->
            _cards.value = decomposeCardsSnapshots(snapshot)
        }
    }

    private fun loadAccounts() {
        /*
        * Get all accounts by the user ID and add to source
        */
        val liveData = AccountDao().getAll(getUserID())
        _accounts.addSource(liveData) { snapshot ->
            _accounts.removeSource(liveData)
            _accounts.value = decomposeAccountsSnapshots(snapshot)
        }
    }

    private fun decomposeAccountsSnapshots(snapshot: DataSnapshot?): List<Account> =
        if (snapshot != null) {
            val accountList = ArrayList<Account>()
            snapshot.children.forEach { postSnapshot ->
                postSnapshot.getValue<Account>()
                    ?.let { accountList.add(it) }
            }
            filterAccounts(accountList)
        } else {
            ArrayList()
        }

    private fun decomposeCardsSnapshots(snapshot: DataSnapshot?): List<Card> =
        if (snapshot != null) {
            val cardList = ArrayList<Card>()
            snapshot.children.forEach { postSnapshot ->
                postSnapshot.getValue<Card>()
                    ?.let { cardList.add(it) }
            }
            filterCards(cardList)
        } else {
            ArrayList()
        }

    private fun filterAccounts(accountList: List<Account>) = accountList.filter {
        filterSearch(it.accountName.toLowerCase(Locale.ROOT))
    }

    private fun filterCards(cardList: List<Card>) = cardList.filter {
        filterSearch(it.entryName.toLowerCase(Locale.ROOT))
    }

    private fun filterSearch(name: String) =
        name.startsWith(_searchQuery) || name.endsWith(_searchQuery) || name.contains(_searchQuery)

    private fun getUserID(): String {
        LocalStorageManager.with(getApplication())
        return LocalStorageManager.get<User>(KEY_USER_ACCOUNT)?.id!!
    }
}