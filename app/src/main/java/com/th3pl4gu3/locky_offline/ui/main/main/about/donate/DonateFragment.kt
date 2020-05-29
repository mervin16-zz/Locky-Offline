package com.th3pl4gu3.locky_offline.ui.main.main.about.donate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.th3pl4gu3.locky_offline.databinding.FragmentDonateBinding

class DonateFragment : Fragment() {

    private var _binding: FragmentDonateBinding? = null
    private var _viewModel: DonateViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDonateBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(DonateViewModel::class.java)

        // Bind lifecycle owner
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Load recyclerview for donation list */
        loadDonations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadDonations() {
        val donationAdapter = DonationItemAdapter(
            DonationClickListener {
            })

        binding.RecyclerViewDonation.apply {
            adapter = donationAdapter
            setHasFixedSize(true)
        }

        donationAdapter.submitList(viewModel.getDonations())

    }
}
