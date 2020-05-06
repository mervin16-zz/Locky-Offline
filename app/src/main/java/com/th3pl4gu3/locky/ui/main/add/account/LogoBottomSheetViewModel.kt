package com.th3pl4gu3.locky.ui.main.add.account

import android.app.Application
import androidx.lifecycle.*
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.repository.network.NetworkRepository
import com.th3pl4gu3.locky.repository.network.WebsiteLogo
import kotlinx.coroutines.launch

class LogoBottomSheetViewModel(application: Application) : AndroidViewModel(application) {

    private val _websites = MutableLiveData<List<WebsiteLogo>>()
    private val _loadingStatus = MutableLiveData<LoadingStatus?>()

    init {
        //Set to null to hit else clause
        _loadingStatus.value = null
    }

    /**
     * Transformations
     **/
    val logoTitle = Transformations.map(_loadingStatus) {
        when (it) {
            LoadingStatus.LOADING -> {
                getString(R.string.message_loading_logo)
            }
            LoadingStatus.DONE -> {
                null
            }
            LoadingStatus.ERROR -> {
                getString(R.string.message_loading_logo_none)
            }
            else -> {
                getString(R.string.text_title_emptyView_logo_search)
            }
        }
    }

    val emptyViewVisibility = Transformations.map(logoTitle) {
        it != null
    }

    /**
     * Properties
     **/
    val websites: LiveData<List<WebsiteLogo>>
        get() = _websites

    /**
     * Functions
     **/
    internal fun setErrorLoadingStatus() {
        _loadingStatus.value = LoadingStatus.ERROR
    }

    internal fun getWebsiteLogoProperties(query: String) = viewModelScope.launch {
        try {
            loadLogos(query)
        } catch (t: Throwable) {
            errorLoadingLogos()
        }
    }

    private suspend fun loadLogos(query: String) {
        _loadingStatus.value = LoadingStatus.LOADING
        _websites.value = NetworkRepository()
            .getWebsiteDetails(query)
        _loadingStatus.value = LoadingStatus.DONE
    }

    private fun errorLoadingLogos() {
        setErrorLoadingStatus()
        _websites.value = ArrayList()
    }

    private fun getString(message: Int) = getApplication<Application>().getString(message)
}