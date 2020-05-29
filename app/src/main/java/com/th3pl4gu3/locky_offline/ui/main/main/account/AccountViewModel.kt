package com.th3pl4gu3.locky_offline.ui.main.main.account

import android.app.Application
import androidx.lifecycle.*
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.AccountSort
import com.th3pl4gu3.locky_offline.repository.LoadingStatus
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.KEY_ACCOUNTS_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import java.util.*

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Live Data Variables
     **/
    private var _showSnackBarEvent = MutableLiveData<String>()
    private var _currentAccountsExposed = MediatorLiveData<List<Account>>()
    private var _loadingStatus = MutableLiveData<LoadingStatus>()
    private var _accountListVisibility = MutableLiveData(false)
    private var _accountEmptyViewVisibility = MutableLiveData(false)
    private var _sort = MutableLiveData(loadSortObject())

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

        /* We load the accounts */
        loadAccounts()
    }

    /**
     * Live Data Transformations
     **/
    val loadingStatus: LiveData<Boolean> = Transformations.map(_loadingStatus) {
        it == LoadingStatus.LOADING
    }

    private val sortedByName = Transformations.map(_currentAccountsExposed) {
        it.sortedBy { account ->
            account.accountName.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByUsername = Transformations.map(_currentAccountsExposed) {
        it.sortedBy { account ->
            account.username.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByEmail = Transformations.map(_currentAccountsExposed) {
        it.sortedBy { account ->
            account.email.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByWebsite = Transformations.map(_currentAccountsExposed) {
        it.sortedBy { account ->
            account.website?.toLowerCase(
                Locale.ROOT
            )
        }
    }
    private val sortedByAuthType = Transformations.map(_currentAccountsExposed) {
        it.sortedBy { account ->
            account.authenticationType?.toLowerCase(
                Locale.ROOT
            )
        }
    }

    val accounts: LiveData<List<Account>> = Transformations.switchMap(_sort) {
        when (true) {
            it.name -> sortedByName
            it.username -> sortedByUsername
            it.email -> sortedByEmail
            it.website -> sortedByWebsite
            it.authType -> sortedByAuthType
            else -> _currentAccountsExposed
        }
    }

    /**
     * Properties
     **/
    val showSnackBarEvent: LiveData<String>
        get() = _showSnackBarEvent

    val accountListVisibility: LiveData<Boolean>
        get() = _accountListVisibility

    val accountEmptyViewVisibility: LiveData<Boolean>
        get() = _accountEmptyViewVisibility


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
    internal fun alternateAccountListVisibility(accountsSize: Int) {
        _accountListVisibility.value = accountsSize > 0
        _accountEmptyViewVisibility.value = accountsSize < 1
    }

    /* Call function whenever there is a change in sorting */
    internal fun sortChange(sort: AccountSort) {
        /*
        * We first save the sort to session
        * Then we change the value of sort
        */
        if (_sort.value.toString() != sort.toString()) {
            saveSortToSession(sort)
            _sort.value = sort
        }
    }

    /* Load the accounts into a mediator live data */
    private fun loadAccounts() {
        /*val liveData = AccountRepository(getApplication()).accounts
        _currentAccountsExposed.addSource(liveData) {
            _currentAccountsExposed.value = it
        }*/

        TODO("Fix")
    }

    /*
    * Checks if sorting session exists
    * If sessions exists, we return the sort object
    * Else we return a new sort object
    */
    private fun loadSortObject(): AccountSort {
        LocalStorageManager.with(getApplication())
        return if (LocalStorageManager.exists(KEY_ACCOUNTS_SORT)) {
            LocalStorageManager.get(KEY_ACCOUNTS_SORT)!!
        } else AccountSort()
    }

    /* Save sort data to Session for persistent re-usability*/
    private fun saveSortToSession(sort: AccountSort) {
        LocalStorageManager.with(getApplication())
        LocalStorageManager.put(KEY_ACCOUNTS_SORT, sort)
    }

}