package com.th3pl4gu3.locky.ui.main.main.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.getValue
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.repository.database.AccountDao

class AccountViewModel : ViewModel(){

    private var _showSnackbarEvent = MutableLiveData<String>()
    private var _loadingStatus = MutableLiveData<LoadingStatus>()
    private val _accountSnapShotList = AccountDao().getAll()
    private val _mediatorAccounts = MediatorLiveData<List<Account>>()

    init {
        //Load the acounts for the first time
        loadAccounts()
    }

    val accounts: LiveData<List<Account>>
        get() = _mediatorAccounts

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

    private fun loadAccounts() {
        _mediatorAccounts.addSource(_accountSnapShotList) { snapshot ->
            if (snapshot != null) {
                val cardList = ArrayList<Account>()
                snapshot.children.forEach { postSnapshot ->
                    postSnapshot.getValue<Account>()?.let { cardList.add(it) }
                }
                _mediatorAccounts.value = cardList
            }
        }
    }
}