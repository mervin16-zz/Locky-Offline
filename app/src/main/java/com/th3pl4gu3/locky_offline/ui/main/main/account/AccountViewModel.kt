package com.th3pl4gu3.locky_offline.ui.main.main.account

import android.app.Application
import androidx.lifecycle.*
import com.th3pl4gu3.locky_offline.core.main.credentials.Account
import com.th3pl4gu3.locky_offline.core.main.tuning.AccountSort
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.AccountRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_ACCOUNTS_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import java.util.*

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    /*
     * Live Data Variables
     */
    private var _loadingStatus = MutableLiveData(Loading.List.LOADING)
    private var _accounts = MediatorLiveData<List<Account>>()
    private var _sort = MutableLiveData(loadSortObject())

    /*
    * Properties
    */
    val loadingStatus: LiveData<Loading.List>
        get() = _loadingStatus

    /*
    * Init clause
    */
    init {

        /*
        * We load the accounts on startup
        */
        loadAccounts()
    }

    /*
    * Transformations
    */
    private val sortedByName = Transformations.map(_accounts) {
        it.sortedBy { account ->
            account.entryName.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByUsername = Transformations.map(_accounts) {
        it.sortedBy { account ->
            account.username.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByEmail = Transformations.map(_accounts) {
        it.sortedBy { account ->
            account.email.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByWebsite = Transformations.map(_accounts) {
        it.sortedBy { account ->
            account.website.toLowerCase(
                Locale.ROOT
            )
        }
    }
    private val sortedByAuthType = Transformations.map(_accounts) {
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
            else -> _accounts
        }
    }

    /*
    * Accessible functions
    */
    internal fun doneLoading(size: Int) {
        _loadingStatus.value = if (size > 0) Loading.List.LIST else Loading.List.EMPTY_VIEW
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

    /*
    * Non-accessible functions
    */
    private fun loadAccounts() {
        _accounts.addSource(
            AccountRepository.getInstance(getApplication()).getAll(activeUser.email)
        ) {
            _accounts.value = it
        }
    }

    /*
    * Checks if sorting session exists
    * If sessions exists, we return the sort object
    * Else we return a new sort object
    */
    private fun loadSortObject(): AccountSort {
        LocalStorageManager.withLogin(getApplication())
        return if (LocalStorageManager.exists(KEY_ACCOUNTS_SORT)) {
            LocalStorageManager.get(KEY_ACCOUNTS_SORT)!!
        } else AccountSort()
    }

    /* Save sort data to Session for persistent re-usability*/
    private fun saveSortToSession(sort: AccountSort) {
        LocalStorageManager.withLogin(getApplication())
        LocalStorageManager.put(KEY_ACCOUNTS_SORT, sort)
    }
}