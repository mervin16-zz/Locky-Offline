package com.th3pl4gu3.locky_offline.ui.main.main.about.donate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.SkuType.INAPP
import com.th3pl4gu3.locky_offline.databinding.FragmentDonateBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.toast

class DonateFragment : Fragment(), PurchasesUpdatedListener {

    private var _binding: FragmentDonateBinding? = null
    private var _viewModel: DonateViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    private lateinit var _billingClient: BillingClient
    private lateinit var _skuList: List<SkuDetails>

    companion object {
        const val TAG = "DONATE_FRAGMENT_TEST"
        private val _skus = listOf(
            "android.test.purchased",
            "android.test.purchased",
            "android.test.canceled",
            "android.test.item_unavailable"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDonateBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(DonateViewModel::class.java)

        // Bind lifecycle owner
        binding.lifecycleOwner = this

        /* Setup billing client */
        setupBillingClient()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadDonations(list: List<SkuDetails>) {
        val donationAdapter = DonationItemAdapter(
            DonationClickListener {
                launchBillingFlow(it)
            })

        binding.RecyclerViewDonation.apply {
            adapter = donationAdapter
            setHasFixedSize(true)
        }

        donationAdapter.submitList(list)

    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult?,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                Log.i(TAG, "Purchase updated: ${purchase.sku} Price: ${purchase.purchaseToken}")
            }
        } else if (billingResult?.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.i(TAG, "Purchase cancelled.")
        } else {
            when (billingResult?.responseCode) {
                BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> Log.i(
                    TAG,
                    "Billing currently unavailable"
                )
                BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> Log.i(
                    TAG,
                    "Billing service has been disconnected."
                )
                BillingClient.BillingResponseCode.SERVICE_TIMEOUT -> Log.i(
                    TAG,
                    "Billing service timeout."
                )
                else -> {
                }
            }
        }
    }


    private fun setupBillingClient() {
        _billingClient = BillingClient
            .newBuilder(requireContext())
            .enablePendingPurchases()
            .setListener(this)
            .build()

        _billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Log.i(TAG, "You've been disconnected.")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.i(TAG, "Success to connect.")

                    /* Load in-app products */
                    loadProducts()

                } else {
                    Log.i(TAG, "Error: ${billingResult.responseCode}")
                }
            }

        })
    }


    private fun loadProducts() {
        if (_billingClient.isReady) {
            val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(_skus)
                .setType(INAPP)
                .build()

            _billingClient.querySkuDetailsAsync(params) { billingResult, list ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    loadProductsToRecyclerView(list)
                } else {
                    Log.i(TAG, "Error querying products")
                }
            }
        } else {
            Log.i(TAG, "Billing client is not ready")
        }
    }

    private fun launchBillingFlow(skuDetails: SkuDetails) {
        val params = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()

        _billingClient.launchBillingFlow(requireActivity(), params)
    }

    private fun loadProductsToRecyclerView(skuList: List<SkuDetails>) {
        loadDonations(skuList)
    }

    private fun toast(message: String) = requireContext().toast(message)
}
