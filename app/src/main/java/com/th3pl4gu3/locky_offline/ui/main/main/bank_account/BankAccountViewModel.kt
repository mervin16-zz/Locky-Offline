package com.th3pl4gu3.locky_offline.ui.main.main.bank_account

import android.app.Application
import androidx.lifecycle.*
import com.th3pl4gu3.locky_offline.core.main.BankAccount
import com.th3pl4gu3.locky_offline.core.main.BankAccountSort
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_BANK_ACCOUNTS_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import kotlinx.coroutines.launch
import java.util.*

class BankAccountViewModel(application: Application) : AndroidViewModel(application) {

    /* Private variables */
    private var _loadingStatus = MutableLiveData(Loading.List.LOADING)
    private var _bankAccounts = MediatorLiveData<List<BankAccount>>()
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
        loadBankAccounts()
    }

    /*
    * Transformations
    */
    private val sortedByName = Transformations.map(_bankAccounts) {
        it.sortedBy { account ->
            account.entryName.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByOwner = Transformations.map(_bankAccounts) {
        it.sortedBy { account ->
            account.accountOwner.toLowerCase(
                Locale.ROOT
            )
        }
    }

    private val sortedByBank = Transformations.map(_bankAccounts) {
        it.sortedBy { account ->
            account.bank.toLowerCase(
                Locale.ROOT
            )
        }
    }

    val bankAccounts: LiveData<List<BankAccount>> = Transformations.switchMap(_sort) {
        when (true) {
            it.accountName -> sortedByName
            it.accountOwner -> sortedByOwner
            it.bank -> sortedByBank
            else -> _bankAccounts
        }
    }

    /*
    * Accessible functions
    */
    internal fun doneLoading(size: Int) {
        _loadingStatus.value = if (size > 0) Loading.List.LIST else Loading.List.EMPTY_VIEW
    }

    /* Call function whenever there is a change in sorting */
    internal fun sortChange(sort: BankAccountSort) {
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
    private fun loadBankAccounts() {
        viewModelScope.launch {
            _bankAccounts.addSource(
                BankAccountRepository.getInstance(getApplication()).getAll(getUser().email)
            ) {
                _bankAccounts.value = it
            }
        }
    }

    /*
    * Checks if sorting session exists
    * If sessions exists, we return the sort object
    * Else we return a new sort object
    */
    private fun loadSortObject(): BankAccountSort {
        LocalStorageManager.withLogin(getApplication())
        return if (LocalStorageManager.exists(KEY_BANK_ACCOUNTS_SORT)) {
            LocalStorageManager.get(KEY_BANK_ACCOUNTS_SORT)!!
        } else BankAccountSort()
    }

    /* Save sort data to Session for persistent re-usability*/
    private fun saveSortToSession(sort: BankAccountSort) {
        LocalStorageManager.withLogin(getApplication())
        LocalStorageManager.put(KEY_BANK_ACCOUNTS_SORT, sort)
    }

    private fun getUser(): User {
        LocalStorageManager.withLogin(getApplication())
        return LocalStorageManager.get<User>(Constants.KEY_USER_ACCOUNT)!!
    }
}