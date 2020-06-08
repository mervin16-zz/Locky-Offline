package com.th3pl4gu3.locky_offline.ui.main.main.about.donate

import android.app.Activity
import android.app.Application
import androidx.lifecycle.*
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.*
import com.th3pl4gu3.locky_offline.repository.billing.BillingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DonateViewModel(application: Application) : AndroidViewModel(application) {

    private var _donations = MutableLiveData<List<SkuDetails>>()
    private var _purchases = MutableLiveData<List<Purchase>>()
    private var _errorOccurred = MutableLiveData<String>()
    private var _responseCode = MutableLiveData<Int>()
    private val _billingRepository: BillingRepository =
        BillingRepository.getInstance(getApplication())

    init {
        _billingRepository.connectBillingClient()
        _donations = _billingRepository.skuDetails
        _purchases = _billingRepository.purchase
        _errorOccurred = _billingRepository.errorOccurred
        _responseCode = _billingRepository.responseCode
    }

    val isDonationVisible = Transformations.map(_donations) {
        it.isNotEmpty()
    }

    val purchases = Transformations.map(_purchases) {
        viewModelScope.launch {
            for (purchase in it) {
                when (purchase.sku) {
                    BillingRepository.COOKIE -> addCookie(Cookie(true))
                    BillingRepository.MILKSHAKE -> addMilkshake(Milkshake(true))
                    BillingRepository.SANDWICH -> addSandwich(Sandwich(true))
                    BillingRepository.BURGER -> addBurger(Burger(true))
                    BillingRepository.GIFT -> addGift(Gift(true))
                    BillingRepository.STAR -> addStar(Star(true))
                    else -> return@launch
                }
            }
        }
    }

    val responseCode: LiveData<String> = Transformations.map(_responseCode) { responseCode ->
        when (responseCode) {
            BillingClient.BillingResponseCode.USER_CANCELED -> getApplication<Application>().getString(
                R.string.message_about_donations_cancelled
            )

            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> getApplication<Application>().getString(
                R.string.message_about_donations_unavailable
            )
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> getApplication<Application>().getString(
                R.string.message_about_donations_billing_unavailable
            )
            else -> getApplication<Application>().getString(
                R.string.error_internal_code_2
            )
        }
    }

    val donations: LiveData<List<SkuDetails>>
        get() = _donations

    val errorOccurred: LiveData<String>
        get() = _errorOccurred

    internal fun launchBillingFlow(activity: Activity, it: SkuDetails) {
        _billingRepository.launchBillingFlow(activity, it)
    }

    internal fun retryConnectingToBilling() {
        _billingRepository.connectBillingClient()
    }

    private suspend fun addCookie(cookie: Cookie) {
        withContext(Dispatchers.IO) {
            _billingRepository.add(cookie)
        }
    }

    private suspend fun addSandwich(sandwich: Sandwich) {
        withContext(Dispatchers.IO) {
            _billingRepository.add(sandwich)
        }
    }

    private suspend fun addMilkshake(milkshake: Milkshake) {
        withContext(Dispatchers.IO) {
            _billingRepository.add(milkshake)
        }
    }

    private suspend fun addBurger(burger: Burger) {
        withContext(Dispatchers.IO) {
            _billingRepository.add(burger)
        }
    }

    private suspend fun addGift(gift: Gift) {
        withContext(Dispatchers.IO) {
            _billingRepository.add(gift)
        }
    }

    private suspend fun addStar(star: Star) {
        withContext(Dispatchers.IO) {
            _billingRepository.add(star)
        }
    }
}