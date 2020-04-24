package com.th3pl4gu3.locky.ui.main.add.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.th3pl4gu3.locky.repository.network.NetworkRepository
import com.th3pl4gu3.locky.repository.network.WebsiteLogo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LogoBottomSheetViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _websites = MutableLiveData<List<WebsiteLogo>>()

    val websites: LiveData<List<WebsiteLogo>>
        get() = _websites

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }

    internal fun resetLogos() {
        _websites.value = null
    }

    internal fun getWebsiteLogoProperties(query: String) = coroutineScope.launch {
        try {
            _websites.value = NetworkRepository()
                .getWebsiteDetails(query)
        } catch (t: Throwable) {
            _websites.value = ArrayList()
        }
    }
}