package com.th3pl4gu3.locky_offline.ui.main.main.bank_account

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.toLiveData
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.BankAccount
import com.th3pl4gu3.locky_offline.core.tuning.BankAccountSort
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.resources
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_BANK_ACCOUNTS_SORT
import kotlinx.coroutines.launch
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
    private val DataSource.Factory<Int, BankAccount>.sortedByName: DataSource.Factory<Int, BankAccount>
        get() {
            return this@sortedByName.mapByPage {
                it.sortedBy { account ->
                    account.entryName.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    private val DataSource.Factory<Int, BankAccount>.sortedByOwner: DataSource.Factory<Int, BankAccount>
        get() {
            return this@sortedByOwner.mapByPage {
                it.sortedBy { account ->
                    account.accountOwner.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    private val DataSource.Factory<Int, BankAccount>.sortedByBank: DataSource.Factory<Int, BankAccount>
        get() {
            return this@sortedByBank.mapByPage {
                it.sortedBy { account ->
                    account.bank.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    val bankAccounts = Transformations.switchMap(_sort) {
        when (true) {
            it.accountName -> _bankAccounts.sortedByName
            it.accountOwner -> _bankAccounts.sortedByOwner
            it.bank -> _bankAccounts.sortedByBank
            else -> _bankAccounts
        }.toLiveData(pageSize = resources.getInteger(R.integer.size_paging_list_default))
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

    internal fun add(bankAccount: BankAccount) =
        viewModelScope.launch {
            BankAccountRepository.getInstance(getApplication()).insert(bankAccount)
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