package com.th3pl4gu3.locky.ui.main.main.search

import android.app.Application
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.BR
import com.th3pl4gu3.locky.core.main.Account
import com.th3pl4gu3.locky.core.main.Card
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.repository.database.AccountDao
import com.th3pl4gu3.locky.repository.database.CardDao
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky.ui.main.utils.ObservableViewModel
import java.util.*
import kotlin.collections.ArrayList

class SearchViewModel(application: Application) : ObservableViewModel(application) {

    /**
     * Variables
     **/
    private val _showSnackbarEvent = MutableLiveData<String>()
    private val _searchQuery = MutableLiveData("")
    private val _cards = MediatorLiveData<List<Card>>()
    private val _accounts = MediatorLiveData<List<Account>>()
    private val _starterScreenVisibility = MutableLiveData(true)
    private val _accountLoadingStatus = MutableLiveData<LoadingStatus>()
    private val _cardLoadingStatus = MutableLiveData<LoadingStatus>()


    /**
     * Bindable data for two way binding
     **/
    var searchQuery: String
        @Bindable get() {
            return _searchQuery.value!!
        }
        set(value) {

            contentLoadingCheck(value)

            checkStarterScreenVisibility()

            _searchQuery.value = value.toLowerCase(Locale.ROOT)
            notifyPropertyChanged(BR.searchQuery)
        }

    /**
     * Transformations
     **/
    val accountListVisibility: LiveData<Boolean> = Transformations.map(_accountLoadingStatus) {
        when (it) {
            LoadingStatus.DONE -> {
                /*
                * If account list is not empty
                * it will show the recyclerview
                * If the card list is empty, it will hide the recycler view
                */
                _accounts.value?.isNotEmpty()
            }

            else -> false
        }
    }

    val cardListVisibility: LiveData<Boolean> = Transformations.map(_cardLoadingStatus) {
        when (it) {
            LoadingStatus.DONE -> {
                /*
                * If card list is not empty
                * it will show the recyclerview
                * If the card list is empty, it will hide the recycler view
                */
                _cards.value?.isNotEmpty()
            }

            else -> false
        }
    }

    /**
     * Properties
     **/
    val showSnackBarEvent: LiveData<String>
        get() = _showSnackbarEvent

    val starterScreenVisibility: LiveData<Boolean>
        get() = _starterScreenVisibility

    val accounts: LiveData<List<Account>>
        get() = _accounts

    val cards: LiveData<List<Card>>
        get() = _cards


    /**
     * Functions
     **/
    internal fun setSnackBarMessage(message: String) {
        _showSnackbarEvent.value = message
    }

    internal fun doneShowingSnackBar() {
        _showSnackbarEvent.value = null
    }

    internal fun doneLoadingCards() {
        /*
        * Set the loading status to DONE
        * This stops the loading for cards
        */
        _cardLoadingStatus.value = LoadingStatus.DONE
    }

    internal fun doneLoadingAccounts() {
        /*
        * Set the loading status to DONE
        * This stops the loading for accounts
        */
        _accountLoadingStatus.value = LoadingStatus.DONE
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

    private fun checkStarterScreenVisibility() {
        if (_starterScreenVisibility.value!!) {
            _starterScreenVisibility.value = false
        }
    }

    private fun loadCards() {
        /*
        * Set the loading status to LOADING
        * This starts the loading for cards
        */
        _cardLoadingStatus.value = LoadingStatus.LOADING

        /*
        * Get all cards by the user ID and add to source
        */
        _cards.addSource(CardDao().getAll(getUserID())) { snapshot ->
            _cards.value = decomposeCardsSnapshots(snapshot)
        }
    }

    private fun loadAccounts() {
        /*
        * Set the loading status to LOADING
        * This starts the loading for accounts
        */
        _accountLoadingStatus.value = LoadingStatus.LOADING

        /*
        * Get all accounts by the user ID and add to source
        */
        _accounts.addSource(AccountDao().getAll(getUserID())) { snapshot ->
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
        name.startsWith(_searchQuery.value!!) || name.endsWith(_searchQuery.value!!) || name.contains(
            _searchQuery.value!!
        )

    private fun getUserID(): String {
        LocalStorageManager.with(getApplication())
        return LocalStorageManager.get<User>(KEY_USER_ACCOUNT)?.id
            ?: TODO("LiveData to catch errors implementation here.")
    }
}