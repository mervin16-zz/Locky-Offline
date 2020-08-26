package com.th3pl4gu3.locky_offline.ui.main.main.bank_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky_offline.core.tuning.BankAccountSort
import com.th3pl4gu3.locky_offline.databinding.FragmentBottomSheetBankAccountTuningBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.isNotInPortrait
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_BANK_ACCOUNTS_SORT

class TuningBankAccountBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBankAccountTuningBinding? = null
    private val binding get() = _binding!!
    private val _sort =
        BankAccountSort()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBankAccountTuningBinding.inflate(inflater, container, false)

        with(binding) {
            LocalStorageManager.withLogin(requireActivity().application)
            sort = LocalStorageManager.get<BankAccountSort>(KEY_BANK_ACCOUNTS_SORT) ?: _sort
            return root
        }
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

        listenerForConfirmChanges()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listenerForConfirmChanges() {
        binding.ButtonChangesConfirm.setOnClickListener {

            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                KEY_BANK_ACCOUNTS_SORT,
                _sort.apply {
                    accountName = binding.ChipSortName.isChecked
                    accountOwner = binding.ChipSortOwner.isChecked
                    bank = binding.ChipSortBank.isChecked
                })

            dismiss()
        }
    }

    private fun bottomSheetConfiguration() {
        if (isNotInPortrait) {
            //This forces the sheet to appear at max height even on landscape
            BottomSheetBehavior.from(requireView().parent as View).state =
                BottomSheetBehavior.STATE_EXPANDED
        }
    }
}