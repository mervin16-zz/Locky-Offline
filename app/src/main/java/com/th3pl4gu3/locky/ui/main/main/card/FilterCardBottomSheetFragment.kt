package com.th3pl4gu3.locky.ui.main.main.card

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky.core.main.CardSort
import com.th3pl4gu3.locky.databinding.FragmentBottomSheetCardFilterBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_CARDS_SORT
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager

class FilterCardBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetCardFilterBinding? = null
    private val binding get() = _binding!!
    private val _sort = CardSort()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetCardFilterBinding.inflate(inflater, container, false)

        with(binding) {

            LocalStorageManager.with(requireActivity().application)

            sort = LocalStorageManager.get<CardSort>(KEY_CARDS_SORT) ?: _sort

            return root
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        with(findNavController().previousBackStackEntry?.savedStateHandle) {
            this?.set(
                KEY_CARDS_SORT,
                _sort.apply {
                    sortByName = binding.ChipSortName.isChecked
                    sortByType = binding.ChipSortType.isChecked
                    sortByBank = binding.ChipSortBank.isChecked
                    sortByCardHolderName = binding.ChipSortCardholder.isChecked
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}