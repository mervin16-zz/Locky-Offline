package com.th3pl4gu3.locky.ui.main.main.card

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky.core.CardRefine
import com.th3pl4gu3.locky.databinding.FragmentBottomSheetCardFilterBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_CARDS_FILTER
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_CARDS_SORT

class FilterCardBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetCardFilterBinding? = null
    private val binding get() = _binding!!
    private val _refineSorting = CardRefine()
    private val _refineFiltering = CardRefine()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetCardFilterBinding.inflate(inflater, container, false)

        with(binding) {

            sort = _refineSorting

            filter = _refineFiltering

            return root
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        with(findNavController().previousBackStackEntry?.savedStateHandle) {
            this?.set(
                KEY_CARDS_FILTER,
                _refineFiltering.apply {
                    cardType = binding.ChipFilterType.isChecked
                    bank = binding.ChipFilterBank.isChecked
                    cardHolderName = binding.ChipFilterCardholder.isChecked
                }
            )

            this?.set(
                KEY_CARDS_SORT,
                _refineSorting.apply {
                    cardType = binding.ChipSortType.isChecked
                    bank = binding.ChipSortBank.isChecked
                    cardHolderName = binding.ChipSortCardholder.isChecked
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}