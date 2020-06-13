package com.th3pl4gu3.locky_offline.ui.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky_offline.databinding.FragmentBottomSheetAddCategoryBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.navigateTo

class AddCategoryBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAddCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetAddCategoryBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onStart() {
        super.onStart()
        //This forces the sheet to appear at max height even on landscape
        BottomSheetBehavior.from(requireView().parent as View).state =
            BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ButtonClose.setOnClickListener {
            dismiss()
        }

        binding.ButtonAddAccount.setOnClickListener {
            navigateTo(AddCategoryBottomSheetFragmentDirections.actionBottomSheetFragmentAddCategoryToFragmentAddAccount())
        }

        binding.ButtonAddCard.setOnClickListener {
            navigateTo(AddCategoryBottomSheetFragmentDirections.actionBottomSheetFragmentAddCategoryToFragmentAddCard())
        }

        binding.ButtonAddBankAccount.setOnClickListener {
            navigateTo(AddCategoryBottomSheetFragmentDirections.actionBottomSheetFragmentAddCategoryToFragmentAddBankAccount())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}