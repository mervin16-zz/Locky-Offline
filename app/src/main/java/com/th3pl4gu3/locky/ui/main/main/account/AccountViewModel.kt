package com.th3pl4gu3.locky.ui.main.main.account

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.core.tuning.AccountSort
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.repository.database.AccountDao
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_ACCOUNTS_SORT
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager
import java.util.*
import kotlin.collections.ArrayList

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private var _showSnackbarEvent = MutableLiveData<String>()
    private var _loadingStatus = MutableLiveData<LoadingStatus>()

    private val _accountSnapShotList = AccountDao().getAll()
    private var _currentAccountsExposed = MediatorLiveData<List<Account>>()

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

    private var _sort = MutableLiveData<AccountSort>()

    init {
        //Load the accounts for the first time
        loadAccounts()
        //Set the default value for account sort
        _sort.value = checkSorting()
    }

    val accounts = Transformations.switchMap(_sort) {
        when (true) {
            it.website -> sortedByWebsite
            it.twofa -> sortedBy2FA
            it.email -> sortedByEmail
            else -> _currentAccountsExposed
        }
    }

    val showSnackBarEvent: LiveData<String>
        get() = _showSnackbarEvent

    val loadingStatus: LiveData<LoadingStatus>
        get() = _loadingStatus

    internal fun setSnackBarMessage(message: String) {
        _showSnackbarEvent.value = message
    }

    internal fun doneShowingSnackBar() {
        _showSnackbarEvent.value = null
    }

    internal fun setLoading(status: LoadingStatus) {
        _loadingStatus.value = status
    }

    internal fun refresh(sort: AccountSort) {
        _sort.value = sort
    }

    private fun loadAccounts() {
        _currentAccountsExposed.addSource(_accountSnapShotList) { snapshot ->
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
                postSnapshot.getValue<Account>()?.let { accountList.add(it) }
            }
            accountList
        } else {
            ArrayList()
        }
}