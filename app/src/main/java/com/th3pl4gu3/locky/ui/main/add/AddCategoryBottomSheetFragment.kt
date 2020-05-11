package com.th3pl4gu3.locky.ui.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky.databinding.FragmentBottomSheetAddCategoryBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ButtonAddAccount.setOnClickListener {
            findNavController().navigate(AddCategoryBottomSheetFragmentDirections.actionBottomSheetFragmentAddCategoryToFragmentAddAccount())
        }

        binding.ButtonAddCard.setOnClickListener {
            findNavController().navigate(AddCategoryBottomSheetFragmentDirections.actionBottomSheetFragmentAddCategoryToFragmentAddCard())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}