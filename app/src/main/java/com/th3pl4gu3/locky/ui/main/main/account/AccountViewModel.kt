package com.th3pl4gu3.locky.ui.main.main.account

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.core.main.Account
import com.th3pl4gu3.locky.core.main.AccountSort
import com.th3pl4gu3.locky.core.main.User
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.repository.database.AccountDao
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_ACCOUNTS_SORT
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_USER_ACCOUNT
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import java.util.*
import kotlin.collections.ArrayList

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Live Data Variables
     **/
    private var _showSnackbarEvent = MutableLiveData<String>()
    private var _loadingStatus = MutableLiveData<LoadingStatus>()
    private var _currentAccountsExposed = MediatorLiveData<List<Account>>()
    private var _sort = MutableLiveData<AccountSort>()
    private var _accountListVisibility = MutableLiveData(false)
    private var _accountEmptyViewVisibility = MutableLiveData(false)

    /**
     * Init Clause
     **/
    init {
        //Set the loading status
        _loadingStatus.value = LoadingStatus.LOADING

        //Load the accounts for the first time
        loadAccounts()

        //Set the default value for account sort
        _sort.value = checkSorting()
    }

    /**
     * Live Data Transformations
     **/
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
    private val sortedBy2FA = Transformations.map(_currentAccountsExposed) {
        it.sortedBy { account ->
            account.twoFA?.toLowerCase(
                Locale.ROOT
            )
        }
    }

    val loadingStatus: LiveData<Boolean> = Transformations.map(_loadingStatus) {
        when (it) {
            LoadingStatus.LOADING -> {
                true
            }
            LoadingStatus.DONE, LoadingStatus.ERROR -> {
                false
            }
            else -> {
                false
            }
        }
    }

    val accounts = Transformations.switchMap(_sort) {
        when (true) {
            it.website -> sortedByWebsite
            it.twofa -> sortedBy2FA
            it.email -> sortedByEmail
            else -> _currentAccountsExposed
        }
    }

    /**
     * Properties
     **/
    val showSnackBarEvent: LiveData<String>
        get() = _showSnackbarEvent

    val accountListVisibility: LiveData<Boolean>
        get() = _accountListVisibility

    val accountEmptyViewVisibility: LiveData<Boolean>
        get() = _accountEmptyViewVisibility

    /**
     * Functions
     **/
    internal fun alternateAccountListVisibility(accountListSize: Int) {
        /**
         * Alternate visibility for account list
         * and empty view for accounts
         */
        if (accountListSize > 0) {
            _accountListVisibility.value = true
            _accountEmptyViewVisibility.value = false

            return
        }

        _accountListVisibility.value = false
        _accountEmptyViewVisibility.value = true
    }

    internal fun setSnackBarMessage(message: String) {
        _showSnackbarEvent.value = message
    }

    internal fun doneShowingSnackBar() {
        _showSnackbarEvent.value = null
    }

    internal fun refresh(sort: AccountSort) {
        _sort.value = sort
    }

    internal fun doneLoading() {
        _loadingStatus.value = LoadingStatus.DONE
    }

    private fun loadAccounts() {
        _currentAccountsExposed.addSource(AccountDao().getAll(getUserID())) { snapshot ->
            _currentAccountsExposed.value = decomposeDataSnapshots(snapshot)
        }
    }

    private fun checkSorting(): AccountSort {
        LocalStorageManager.with(getApplication())
        return if (LocalStorageManager.exists(KEY_ACCOUNTS_SORT)) {
            LocalStorageManager.get(KEY_ACCOUNTS_SORT)!!
        } else AccountSort()
    }

    private fun decomposeDataSnapshots(snapshot: DataSnapshot?): List<Account> =
        if (snapshot != null) {
            val accountList = ArrayList<Account>()
            snapshot.children.forEach { postSnapshot ->
                postSnapshot.getValue<Account>()
                    ?.let { accountList.add(it) }
            }
            accountList
        } else {
            ArrayList()
        }

    private fun getUserID(): String {
        LocalStorageManager.with(getApplication())
        return LocalStorageManager.get<User>(KEY_USER_ACCOUNT)?.id
            ?: TODO("LiveData to catch errors implementation here.")
    }
}