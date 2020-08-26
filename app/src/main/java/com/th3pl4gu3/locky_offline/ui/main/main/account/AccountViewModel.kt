package com.th3pl4gu3.locky_offline.ui.main.main.account

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.toLiveData
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Account
import com.th3pl4gu3.locky_offline.core.tuning.AccountSort
import com.th3pl4gu3.locky_offline.repository.Loading
import com.th3pl4gu3.locky_offline.repository.database.repositories.AccountRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.resources
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_ACCOUNTS_SORT
import kotlinx.coroutines.launch
import java.util.*

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    /*
     * Live Data Variables
     */
    private var _loadingStatus = MutableLiveData(Loading.List.LOADING)
    private var _accounts = AccountRepository.getInstance(getApplication()).getAll(activeUser.email)
    private var _sort = MutableLiveData(loadSortObject())

    /*
    * Properties
    */
    val loadingStatus: LiveData<Loading.List>
        get() = _loadingStatus

    private val DataSource.Factory<Int, Account>.sortByEntryName: DataSource.Factory<Int, Account>
        get() {
            return this@sortByEntryName.mapByPage { list ->
                list.sortedBy { account ->
                    account.entryName.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    private val DataSource.Factory<Int, Account>.sortByUsername: DataSource.Factory<Int, Account>
        get() {
            return this@sortByUsername.mapByPage { list ->
                list.sortedBy { account ->
                    account.username.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    private val DataSource.Factory<Int, Account>.sortByEmail: DataSource.Factory<Int, Account>
        get() {
            return this@sortByEmail.mapByPage { list ->
                list.sortedBy { account ->
                    account.email.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    private val DataSource.Factory<Int, Account>.sortByWebsite: DataSource.Factory<Int, Account>
        get() {
            return this@sortByWebsite.mapByPage { list ->
                list.sortedBy { account ->
                    account.website.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    private val DataSource.Factory<Int, Account>.sortByAuthenticationType: DataSource.Factory<Int, Account>
        get() {
            return this@sortByAuthenticationType.mapByPage { list ->
                list.sortedBy { account ->
                    account.authenticationType?.toLowerCase(
                        Locale.ROOT
                    )
                }
            }
        }

    /*
    * Transformations
    */
    val accounts = Transformations.switchMap(_sort) {
        when (true) {
            it.name -> _accounts.sortByEntryName
            it.username -> _accounts.sortByUsername
            it.email -> _accounts.sortByEmail
            it.website -> _accounts.sortByWebsite
            it.authType -> _accounts.sortByAuthenticationType
            else -> _accounts
        }.toLiveData(pageSize = resources.getInteger(R.integer.size_paging_list_default))
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

    internal fun add(account: Account) =
        viewModelScope.launch { AccountRepository.getInstance(getApplication()).insert(account) }

    /*
    * Non-accessible functions
    */
    /*
    * Checks if sorting session exists
    * If sessions exists, we return the sort object
    * Else we return a new sort object
    */
    private fun loadSortObject(): AccountSort = with(LocalStorageManager) {
        withLogin(getApplication())
        return if (exists(KEY_ACCOUNTS_SORT)) {
            get(KEY_ACCOUNTS_SORT)!!
        } else AccountSort()
    }

    /* Save sort data to Session for persistent re-usability*/
    private fun saveSortToSession(sort: AccountSort) = with(LocalStorageManager) {
        withLogin(getApplication())
        put(KEY_ACCOUNTS_SORT, sort)
    }
}