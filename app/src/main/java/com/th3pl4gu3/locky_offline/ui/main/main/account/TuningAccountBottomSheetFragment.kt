package com.th3pl4gu3.locky_offline.ui.main.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky_offline.core.tuning.AccountSort
import com.th3pl4gu3.locky_offline.databinding.FragmentBottomSheetAccountTuningBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.isNotInPortrait
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_ACCOUNTS_SORT

class TuningAccountBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAccountTuningBinding? = null
    private val binding get() = _binding!!
    private val _sort =
        AccountSort()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetAccountTuningBinding.inflate(inflater, container, false)

        with(binding) {
            LocalStorageManager.withLogin(requireActivity().application)
            sort = LocalStorageManager.get<AccountSort>(KEY_ACCOUNTS_SORT) ?: _sort
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
                KEY_ACCOUNTS_SORT,
                _sort.apply {
                    name = binding.ChipSortName.isChecked
                    username = binding.ChipSortUsername.isChecked
                    email = binding.ChipSortEmail.isChecked
                    website = binding.ChipSortWebsite.isChecked
                    authType = binding.ChipSortAuthType.isChecked
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