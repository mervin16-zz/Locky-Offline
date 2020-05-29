package com.th3pl4gu3.locky_offline.ui.main.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentBottomSheetAddCategoryBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.toast

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

        binding.ButtonClose.setOnClickListener {
            dismiss()
        }

        binding.ButtonAddAccount.setOnClickListener {
            findNavController().navigate(AddCategoryBottomSheetFragmentDirections.actionBottomSheetFragmentAddCategoryToFragmentAddAccount())
        }

        binding.ButtonAddCard.setOnClickListener {
            findNavController().navigate(AddCategoryBottomSheetFragmentDirections.actionBottomSheetFragmentAddCategoryToFragmentAddCard())
        }

        binding.ButtonAddDevice.setOnClickListener {
            toast(getString(R.string.dev_feature_implementation_unknown, "Add device"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toast(message: String) = requireContext().toast(message)
}