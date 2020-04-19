package com.th3pl4gu3.locky.ui.main.add.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky.databinding.FragmentBottomSheetAccountLogoBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_ACCOUNT_LOGO

class LogoBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAccountLogoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetAccountLogoBinding.inflate(inflater, container, false)

        binding.visa.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                KEY_ACCOUNT_LOGO,
                "https://logo.clearbit.com/visa.com"
            )
            dismiss()
        }

        binding.mc.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                KEY_ACCOUNT_LOGO,
                "https://logo.clearbit.com/mastercard.com"
            )
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}