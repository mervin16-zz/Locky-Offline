package com.th3pl4gu3.locky_offline.ui.main.main.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky_offline.core.tuning.CardSort
import com.th3pl4gu3.locky_offline.databinding.FragmentBottomSheetCardTuningBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.isNotInPortrait
import com.th3pl4gu3.locky_offline.ui.main.utils.helpers.LocalStorageManager
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_CARDS_SORT

class TuningCardBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetCardTuningBinding? = null
    private val binding get() = _binding!!
    private val _sort = CardSort()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetCardTuningBinding.inflate(inflater, container, false)

        with(binding) {
            LocalStorageManager.withLogin(requireActivity().application)
            sort = LocalStorageManager.get<CardSort>(KEY_CARDS_SORT) ?: _sort
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
                KEY_CARDS_SORT,
                _sort.apply {
                    sortByName = binding.ChipSortEntryName.isChecked
                    sortByBank = binding.ChipSortBank.isChecked
                    sortByType = binding.ChipSortType.isChecked
                    sortByCardHolderName = binding.ChipSortCardholderName.isChecked
                }
            )

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