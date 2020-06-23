package com.th3pl4gu3.locky_offline.ui.main.main.about.donate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentDonateBinding
import com.th3pl4gu3.locky_offline.repository.billing.AugmentedSkuDetails
import com.th3pl4gu3.locky_offline.ui.main.utils.action
import com.th3pl4gu3.locky_offline.ui.main.utils.snackbar
import com.th3pl4gu3.locky_offline.ui.main.utils.toast

class DonateFragment : Fragment() {

    private var _binding: FragmentDonateBinding? = null
    private var _viewModel: DonateViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    companion object {
        const val TAG = "DONATE_FRAGMENT_TEST"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDonateBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(DonateViewModel::class.java)
        // Bind viewModel
        binding.viewModel = viewModel
        // Bind lifecycle owner
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Observe donations event */
        observeDonations()

        /* Observe purchases */
        observePurchases()

        /* Observe error messages returned by billing repo */
        observeErrorMessage()

        /* Observe response codes returned by billing repo */
        observeResponseCodeMessage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadProductsToRecyclerView(skuList: List<AugmentedSkuDetails>) {
        val donationAdapter = DonationItemAdapter(
            DonationClickListener {
                viewModel.launchBillingFlow(requireActivity(), it)
            })

        binding.RecyclerViewDonation.apply {
            adapter = donationAdapter
            setHasFixedSize(true)
        }

        donationAdapter.submitList(skuList.sortedBy {
            it.price
        })
    }

    private fun observeDonations() {
        viewModel.donations.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                loadProductsToRecyclerView(it)
            }
        })
    }

    private fun observePurchases() {
        viewModel.purchases.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toast(it)
            }
        })
    }

    private fun observeErrorMessage() {
        viewModel.errorOccurred.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.LayoutParentDonate.snackbar(it) {
                    action(getString(R.string.button_action_retry)) {
                        viewModel.retryConnectingToBilling()
                    }
                }
            }
        })
    }

    private fun observeResponseCodeMessage() {
        viewModel.responseCode.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toast(it)
            }
        })
    }

    private fun toast(message: String) = requireContext().toast(message)
}
