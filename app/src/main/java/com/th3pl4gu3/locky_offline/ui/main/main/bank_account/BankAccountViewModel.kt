package com.th3pl4gu3.locky_offline.ui.main.main.bank_account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.th3pl4gu3.locky_offline.core.main.credentials.BankAccount
import com.th3pl4gu3.locky_offline.core.main.tuning.BankAccountSort
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_BANK_ACCOUNTS_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import java.util.*

class BankAccountViewModel(application: Application) : AndroidViewModel(application) {

    /* Private variables */
    private var _loadingStatus = MutableLiveData(Loading.List.LOADING)
    private var _bankAccounts =
        BankAccountRepository.getInstance(getApplication()).getAll(activeUser.email)
    private var _sort = MutableLiveData(loadSortObject())

    /*
    * Properties
    */
    val loadingStatus: LiveData<Loading.List>
        get() = _loadingStatus

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
    /*
    * Checks if sorting session exists
    * If sessions exists, we return the sort object
    * Else we return a new sort object
    */
    private fun loadSortObject(): BankAccountSort = with(LocalStorageManager) {
        withLogin(getApplication())
        return if (exists(KEY_BANK_ACCOUNTS_SORT)) {
            get(KEY_BANK_ACCOUNTS_SORT)!!
        } else BankAccountSort()
    }

    /* Save sort data to Session for persistent re-usability*/
    private fun saveSortToSession(sort: BankAccountSort) = with(LocalStorageManager) {
        withLogin(getApplication())
        put(KEY_BANK_ACCOUNTS_SORT, sort)
    }
}