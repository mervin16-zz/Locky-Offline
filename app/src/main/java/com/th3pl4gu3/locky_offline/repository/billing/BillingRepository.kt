package com.th3pl4gu3.locky_offline.repository.billing

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.*

class BillingRepository constructor(private val application: Application) :
    PurchasesUpdatedListener, BillingClientStateListener, PurchaseHistoryResponseListener {

    private val database = BillingDatabase.getInstance(application)
    private val donationDao = database.donationDao()
    private lateinit var _billingClient: BillingClient

    var skuDetails = MutableLiveData<List<SkuDetails>>()
    var purchase = MutableLiveData<List<Purchase>>()
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
            purchase.value = purchases
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED && purchases != null) {
            purchase.value = purchases
        } else {
            responseCode.value = billingResult.responseCode
        }
    }

    override fun onPurchaseHistoryResponse(
        billingResult: BillingResult?,
        purchases: MutableList<PurchaseHistoryRecord>?
    ) {
        TODO("Not yet implemented")
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
        errorOccurred.value =
            application.getString(R.string.message_about_donations_billing_disconnected)
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

    suspend fun add(cookie: Cookie) = donationDao.insert(cookie)

    suspend fun add(sandwich: Sandwich) = donationDao.insert(sandwich)

    suspend fun add(milkshake: Milkshake) = donationDao.insert(milkshake)

    suspend fun add(burger: Burger) = donationDao.insert(burger)

    suspend fun add(gift: Gift) = donationDao.insert(gift)

    suspend fun add(star: Star) = donationDao.insert(star)

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