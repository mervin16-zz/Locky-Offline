package com.th3pl4gu3.locky_offline.ui.main.add.bank_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky_offline.databinding.FragmentBottomSheetBankAccountLogoBinding
import com.th3pl4gu3.locky_offline.ui.main.add.LogoAccentAdapter
import com.th3pl4gu3.locky_offline.ui.main.add.LogoAccentClickListener
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.isNotInPortrait
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_BANK_ACCOUNT_LOGO_HEX

class LogoAccentBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBankAccountLogoBinding? = null
    private var _viewModel: LogoAccentBottomSheetViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBankAccountLogoBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(LogoAccentBottomSheetViewModel::class.java)
        /*
        * Fetch the current hex accent
        */
        binding.currentHex = LogoAccentBottomSheetFragmentArgs.fromBundle(requireArguments()).hexcurrent
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        /*
        * We check if device is in landscape
        * If it is in landscape,
        * We expand the height of the bottom sheet
        */
        bottomSheetConfiguration()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * Display the available hex colors
        */
        subscribeLogoList(viewModel.getAvailableHexColors())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeLogoList(hex: List<String>){
        val adapter = LogoAccentAdapter(
            LogoAccentClickListener {
                passLogoHexToParentFragmentAndDismiss(it)
            })

        binding.RecyclerViewLogo.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 4)
            this.adapter = adapter
        }

        adapter.submitList(hex)
    }

    private fun passLogoHexToParentFragmentAndDismiss(hex: String) {
        //Pass logo url to parent fragment
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            KEY_BANK_ACCOUNT_LOGO_HEX,
            hex
        )
        dismiss()
    }

    private fun bottomSheetConfiguration() {
        if (isNotInPortrait) {
            //This forces the sheet to appear at max height even on landscape
            BottomSheetBehavior.from(requireView().parent as View).state =
                BottomSheetBehavior.STATE_EXPANDED
        }
    }
}