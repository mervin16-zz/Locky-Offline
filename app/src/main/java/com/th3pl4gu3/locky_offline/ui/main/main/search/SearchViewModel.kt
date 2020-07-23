package com.th3pl4gu3.locky_offline.ui.main.main.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.credentials.CredentialIdentifier
import com.th3pl4gu3.locky_offline.repository.database.repositories.AccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.BankAccountRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.CardRepository
import com.th3pl4gu3.locky_offline.repository.database.repositories.DeviceRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.activeUser
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.resources

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    /*
    * Private variables
    */
    private val _starterScreenVisibility = MutableLiveData(true)
    private var _filter = MutableLiveData(CredentialIdentifier.ACCOUNTS)
    private var _searchQuery = MutableLiveData<String>()
    private var _resultSize = MutableLiveData<Int>()

    /*
    * Properties
    */
    val starterScreenVisibility: LiveData<Boolean>
        get() = _starterScreenVisibility

    /*
    *Transformations
    */
    val accounts = Transformations.switchMap(_searchQuery) { query ->
        if (_filter.value == CredentialIdentifier.ACCOUNTS) {
            AccountRepository.getInstance(getApplication())
                .getAll(activeUser.email).mapByPage { list ->
                    list.filter {
                        it.entryName.contains(query)
                                ||
                                it.username.contains(query)
                                ||
                                it.email.contains(query)
                                ||
                                it.website.contains(query)
                    }
                }.toLiveData(pageSize = resources.getInteger(R.integer.size_paging_list_search))
        } else {
            null
        }
    }

    val cards = Transformations.switchMap(_searchQuery) {
        if (_filter.value == CredentialIdentifier.CARDS) {
            CardRepository.getInstance(getApplication())
                .search(it, activeUser.email)
                .toLiveData(pageSize = resources.getInteger(R.integer.size_paging_list_search))
        } else {
            null
        }
    }

    val bankAccounts = Transformations.switchMap(_searchQuery) {
        if (_filter.value == CredentialIdentifier.BANK_ACCOUNTS) {
            BankAccountRepository.getInstance(getApplication())
                .search(it, activeUser.email)
                .toLiveData(pageSize = resources.getInteger(R.integer.size_paging_list_search))
        } else {
            null
        }
    }

    val devices = Transformations.switchMap(_searchQuery) {
        if (_filter.value == CredentialIdentifier.DEVICES) {
            DeviceRepository.getInstance(getApplication())
                .search(it, activeUser.email)
                .toLiveData(pageSize = resources.getInteger(R.integer.size_paging_list_search))
        } else {
            null
        }
    }

    val resultSize = Transformations.map(_resultSize) {
        getApplication<Application>().resources.getQuantityString(
            R.plurals.message_search_results,
            it,
            it
        )
    }

    val filterText = Transformations.map(_filter) {
        when (it) {
            CredentialIdentifier.ACCOUNTS -> getApplication<Application>().getString(R.string.menu_search_filters_account)
            CredentialIdentifier.BANK_ACCOUNTS -> getApplication<Application>().getString(R.string.menu_search_filters_bank_account)
            CredentialIdentifier.CARDS -> getApplication<Application>().getString(R.string.menu_search_filters_card)
            CredentialIdentifier.DEVICES -> getApplication<Application>().getString(R.string.menu_search_filters_device)
            else -> getApplication<Application>().getString(R.string.menu_search_filters_account)
        }
    }

    /*
    * Accessible functions
    */
    internal fun updateResultSize(newSize: Int) {
        _resultSize.value = newSize
    }

    internal fun setFilter(value: CredentialIdentifier) {
        _filter.value = value
        _searchQuery.value = _searchQuery.value //To trigger transformations
    }

    internal fun search(query: String) {
        /* First we check if starter screen is hidden
             * If not hidden, we hide starter screen
             * The live data will automatically make the lists visible
            */
        hideStarterScreen()

        _searchQuery.value = "%$query%"
        _filter.value = _filter.value //To trigger filter
    }

    internal fun cancel() {
        /*If user clicks on close button
        * we check if starter screen is hidden
        * If it is hidden, we show starter screen
        * The live data will automatically make the lists gone
        */
        showStarterScreen()
    }


    /*
    * In-accessible functions
    */
    private fun showStarterScreen() {
        if (!_starterScreenVisibility.value!!) {
            _starterScreenVisibility.value = true
        }
    }

    private fun hideStarterScreen() {
        if (_starterScreenVisibility.value!!) {
            _starterScreenVisibility.value = false
        }
    }
}