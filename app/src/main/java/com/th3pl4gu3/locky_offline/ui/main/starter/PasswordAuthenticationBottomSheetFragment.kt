package com.th3pl4gu3.locky_offline.ui.main.starter

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentBottomSheetAuthenticationPasswordBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.hash
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.isNotInPortrait
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast

class PasswordAuthenticationBottomSheetFragment :
    BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAuthenticationPasswordBinding? = null
    private lateinit var _savedPassword: String

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Bind the layout */
        _binding =
            FragmentBottomSheetAuthenticationPasswordBinding.inflate(inflater, container, false)
        /* Get the saved password */
        _savedPassword =
            PasswordAuthenticationBottomSheetFragmentArgs.fromBundle(requireArguments()).valuesavedpassword
        /* Bind to this to lifecycle owner*/
        binding.lifecycleOwner = this

        return binding.root
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

        /*
        * Confirm button click listener
        */
        confirmClickListener()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        toast(getString(R.string.error_biometric_authentication_cancelled))

        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bottomSheetConfiguration() {
        if (isNotInPortrait) {
            //This forces the sheet to appear at max height even on landscape
            BottomSheetBehavior.from(requireView().parent as View).state =
                BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun confirmClickListener() {
        binding.ButtonConfirm.setOnClickListener {
            validateMasterPassword()
        }
    }

    private fun validateMasterPassword() {
        with(binding.MasterPassword) {
            if (this.editText?.text.toString().hash != _savedPassword) {
                this.error = getString(R.string.error_field_validation_password_notmatch)
            } else {
                navigateTo(PasswordAuthenticationBottomSheetFragmentDirections.actionFragmentBottomDialogPasswordAuthToFragmentAccount())
            }
        }
    }
}