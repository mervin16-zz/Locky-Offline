package com.th3pl4gu3.locky_offline.repository.billing

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.th3pl4gu3.locky_offline.R

/*
* Billing repository to handle all in-app products of Locky
* Instantiates and handle callbacks of the Google play billing
*/
class BillingRepository constructor(private val application: Application) :
    PurchasesUpdatedListener, BillingClientStateListener {

    //FIXME("Fix Billing Repository Issues")
    private lateinit var _billingClient: BillingClient

    var skuDetails = MutableLiveData<List<AugmentedSkuDetails>>()
    var isPurchaseCompleted = MutableLiveData<Boolean>()
    var errorOccurred = MutableLiveData<String>()
    var responseCode = MutableLiveData<Int>()

    companion object {
        private const val TAG = "BILLING_REPOSITORY"
        internal const val COOKIE = "com.th3pl4gu3.locky_offline.cookie01"
        internal const val MILKSHAKE = "com.th3pl4gu3.locky_offline.milkshake01"
        internal const val SANDWICH = "com.th3pl4gu3.locky_offline.sandwich01"
        internal const val BURGER = "com.th3pl4gu3.locky_offline.burger01"
        internal const val GIFT = "com.th3pl4gu3.locky_offline.gift01"
        internal const val STAR = "com.th3pl4gu3.locky_offline.star01"

        private val _skus = listOf(COOKIE, MILKSHAKE, SANDWICH, BURGER, GIFT, STAR)

        @Volatile
        private var INSTANCE: BillingRepository? = null

        fun getInstance(application: Application): BillingRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: BillingRepository(application)
                        .also { INSTANCE = it }
            }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                isPurchaseCompleted.value =
                    purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            }
        } else {
            responseCode.value = billingResult.responseCode
        }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult?) {
        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK) {
            /* Load in-app products */
            loadProducts()
        } else {
            errorOccurred.value =
                application.getString(R.string.error_about_donations_querying_products)
        }
    }

    override fun onBillingServiceDisconnected() {
        errorOccurred(application.getString(R.string.message_about_donations_billing_disconnected))
    }

    internal fun connectBillingClient() {
        _billingClient = BillingClient
            .newBuilder(application.applicationContext)
            .enablePendingPurchases()
            .setListener(this)
            .build()

        _billingClient.startConnection(this)
    }

    internal fun launchBillingFlow(activity: Activity, augmentedSkuDetails: AugmentedSkuDetails) {
        val params = BillingFlowParams.newBuilder()
            .setSkuDetails(augmentedSkuDetails.getSkuDetails())
            .build()

        _billingClient.launchBillingFlow(activity, params)
    }

    private fun loadProducts() {
        if (_billingClient.isReady) {
            val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(_skus)
                .setType(BillingClient.SkuType.INAPP)
                .build()

            _billingClient.querySkuDetailsAsync(params) { billingResult, list ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    skuDetails.value = translateSkuDetailsToAugmented(list)
                } else {
                    errorOccurred(application.getString(R.string.error_about_donations_querying_products))
                }
            }
        } else {
            errorOccurred(application.getString(R.string.message_about_donations_billing_disconnected))
        }
    }

    /* Converts Sku Details to Augmented Sku Details*/
    private fun translateSkuDetailsToAugmented(list: List<SkuDetails>) =
        ArrayList<AugmentedSkuDetails>().apply {
            for (skuDetails in list) {
                add(
                    AugmentedSkuDetails.translate(
                        false,
                        skuDetails
                    )
                )
            }
        }

    /* Other functions */
    private fun errorOccurred(errorMessage: String) {
        errorOccurred.value = errorMessage
    }
}