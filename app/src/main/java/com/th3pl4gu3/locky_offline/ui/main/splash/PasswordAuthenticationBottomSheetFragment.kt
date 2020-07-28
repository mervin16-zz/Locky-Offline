package com.th3pl4gu3.locky_offline.ui.main.splash

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentBottomSheetAuthenticationPasswordBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.SETTINGS_CRYPTO_DIGEST_SCHEME
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.isNotInPortrait
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast
import java.security.MessageDigest

class PasswordAuthenticationBottomSheetFragment(private val savedPassword: String?) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetAuthenticationPasswordBinding? = null

    private val binding get() = _binding!!

    private val splashActivity: SplashActivity
        get() = requireActivity() as SplashActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentBottomSheetAuthenticationPasswordBinding.inflate(inflater, container, false)
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

        toast(getString(R.string.error_biometric_authentication_failed))

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
            val enteredPasswordDigest = String(
                MessageDigest.getInstance(SETTINGS_CRYPTO_DIGEST_SCHEME)
                    .digest(this.editText?.text.toString().toByteArray())
            )
            if (enteredPasswordDigest != savedPassword) {
                this.error = getString(R.string.error_field_validation_password_notmatch)
            } else {
                this.error = null
                splashActivity.viewModel.canNavigateToMainScreen.value = true
            }
        }
    }
}