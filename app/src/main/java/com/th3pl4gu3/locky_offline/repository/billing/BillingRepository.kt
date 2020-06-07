package com.th3pl4gu3.locky_offline.repository.billing

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.*

class BillingRepository constructor(private val application: Application) :
    PurchasesUpdatedListener, BillingClientStateListener {

    private val database = BillingDatabase.getInstance(application)
    private val donationDao = database.donationDao()
    private lateinit var _billingClient: BillingClient

    var skuDetails = MutableLiveData<List<SkuDetails>>()
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
            handlePurchase(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED && purchases != null) {
            handlePurchase(purchases)
        } else {
            responseCode.value = billingResult.responseCode
        }
    }

    private fun handlePurchase(purchases: MutableList<Purchase>) {
        for (purchase in purchases) {
            when (purchase.sku) {
                COOKIE -> donationDao.insert(Cookie(true))
                MILKSHAKE -> donationDao.insert(Milkshake(true))
                SANDWICH -> donationDao.insert(Sandwich(true))
                BURGER -> donationDao.insert(Burger(true))
                GIFT -> donationDao.insert(Gift(true))
                STAR -> donationDao.insert(Star(true))
                else -> return
            }
        }
    }

    override fun onBillingServiceDisconnected() {
        errorOccurred.value =
            application.getString(R.string.message_about_donations_billing_disconnected)
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

    fun connectBillingClient() {
        _billingClient = BillingClient
            .newBuilder(application.applicationContext)
            .enablePendingPurchases()
            .setListener(this)
            .build()

        _billingClient.startConnection(this)
    }

    fun launchBillingFlow(activity: Activity, skuDetails: SkuDetails) {
        val params = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
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
                    skuDetails.value = list
                } else {
                    errorOccurred.value =
                        application.getString(R.string.error_about_donations_querying_products)
                }
            }
        } else {
            errorOccurred.value =
                application.getString(R.string.message_about_donations_billing_disconnected)
        }
    }
}