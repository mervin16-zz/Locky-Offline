package com.th3pl4gu3.locky_offline.ui.main.main.about.donate

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.repository.billing.BillingRepository

class DonateViewModel(application: Application) : AndroidViewModel(application) {

    private var _donations = MutableLiveData<List<SkuDetails>>()
    private var _errorOccurred = MutableLiveData<String>()
    private var _responseCode = MutableLiveData<Int>()
    private val _billingRepository: BillingRepository =
        BillingRepository.getInstance(getApplication())

    init {
        _billingRepository.connectBillingClient()
        _donations = _billingRepository.skuDetails
        _errorOccurred = _billingRepository.errorOccurred
        _responseCode = _billingRepository.responseCode
    }

    val isDonationVisible = Transformations.map(_donations) {
        it.isNotEmpty()
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

}