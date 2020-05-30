package com.th3pl4gu3.locky_offline.ui.main.main.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.core.main.User
import com.th3pl4gu3.locky_offline.repository.database.AccountRepository
import com.th3pl4gu3.locky_offline.repository.database.CardRepository
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants
import com.th3pl4gu3.locky_offline.ui.main.utils.LocalStorageManager
import java.util.*

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _showSnackbarEvent = MutableLiveData<String>()
    private val _starterScreenVisibility = MutableLiveData(true)
    private var _searchQuery = MutableLiveData<String>()


    val showSnackBarEvent: LiveData<String>
        get() = _showSnackbarEvent

    val starterScreenVisibility: LiveData<Boolean>
        get() = _starterScreenVisibility

    val accounts: LiveData<List<Account>> = Transformations.switchMap(_searchQuery) {
        AccountRepository.getInstance(getApplication()).search(it, getUser().email)
    }

    val cards: LiveData<List<Card>> = Transformations.switchMap(_searchQuery) {
        CardRepository.getInstance(getApplication()).search(it, getUser().email)
    }

    val accountsSize: LiveData<String> = Transformations.map(accounts) {
        getApplication<Application>().getString(
            R.string.text_subtitle_search_accounts,
            it.size.toString()
        )
    }

    val cardsSize: LiveData<String> = Transformations.map(cards) {
        getApplication<Application>().getString(
            R.string.text_subtitle_search_cards,
            it.size.toString()
        )
    }

    val isAccountListVisible: LiveData<Boolean> = Transformations.map(accounts) {
        it.isNotEmpty()
    }

    val isCardListVisible: LiveData<Boolean> = Transformations.map(cards) {
        it.isNotEmpty()
    }

    internal fun setSnackBarMessage(message: String) {
        _showSnackbarEvent.value = message
    }

    internal fun doneShowingSnackBar() {
        _showSnackbarEvent.value = null
    }

    internal fun search(query: String) {
        /* First we check if starter screen is hidden
        * If not hidden, we hide starter screen
        * The live data will automatically make the lists visible
        */
        hideStarterScreen()

        _searchQuery.value = "%${query.trim().toLowerCase(Locale.ROOT)}%"
    }

    internal fun cancel() {
        /*If user clicks on close button
        * we check if starter screen is hidden
        * If it is hidden, we show starter screen
        * The live data will automatically make the lists gone
        */
        showStarterScreen()
    }

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

    private fun getUser(): User {
        LocalStorageManager.withLogin(getApplication())
        return LocalStorageManager.get<User>(Constants.KEY_USER_ACCOUNT)!!
    }
}