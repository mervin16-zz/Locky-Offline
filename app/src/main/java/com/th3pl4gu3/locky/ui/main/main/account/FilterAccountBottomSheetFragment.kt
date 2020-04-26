package com.th3pl4gu3.locky.ui.main.main.account

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky.core.tuning.AccountSort
import com.th3pl4gu3.locky.databinding.FragmentBottomSheetAccountFilterBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_ACCOUNTS_SORT
import com.th3pl4gu3.locky.ui.main.utils.LocalStorageManager

class FilterAccountBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAccountFilterBinding? = null
    private val binding get() = _binding!!
    private val _sort = AccountSort()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetAccountFilterBinding.inflate(inflater, container, false)

        with(binding) {

            LocalStorageManager.with(requireActivity().application)

            sort = LocalStorageManager.get<AccountSort>(KEY_ACCOUNTS_SORT) ?: _sort

            return root
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        with(findNavController().previousBackStackEntry?.savedStateHandle) {

            this?.set(
                KEY_ACCOUNTS_SORT,
                _sort.apply {
                    website = binding.ChipSortWebsite.isChecked
                    email = binding.ChipSortEmail.isChecked
                    twofa = binding.ChipSort2FA.isChecked
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}