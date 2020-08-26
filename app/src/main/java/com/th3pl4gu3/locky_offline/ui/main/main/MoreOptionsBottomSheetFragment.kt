package com.th3pl4gu3.locky_offline.ui.main.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Account
import com.th3pl4gu3.locky_offline.core.credentials.BankAccount
import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.core.credentials.Device
import com.th3pl4gu3.locky_offline.databinding.CustomViewBottomSheetMoreoptionsBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.copyToClipboard
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.isNotInPortrait
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_CREDENTIAL_RESTORE
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.LockyUtil

class MoreOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: CustomViewBottomSheetMoreoptionsBinding? = null
    private var _viewModel: MoreOptionsViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomViewBottomSheetMoreoptionsBinding.inflate(
            inflater,
            container,
            false
        )
        _viewModel = ViewModelProvider(this).get(MoreOptionsViewModel::class.java)

        /* Fetch all possible values given */
        val account = MoreOptionsBottomSheetFragmentArgs.fromBundle(
            requireArguments()
        ).valueaccount
        val card = MoreOptionsBottomSheetFragmentArgs.fromBundle(
            requireArguments()
        ).valuecard
        val bankAccount =
            MoreOptionsBottomSheetFragmentArgs.fromBundle(
                requireArguments()
            ).valuebankaccount
        val device = MoreOptionsBottomSheetFragmentArgs.fromBundle(
            requireArguments()
        ).valuedevice

        with(binding.NavigationViewMoreOptions) {
            when {
                account != null -> accountConfiguration(this, account)
                card != null -> cardConfiguration(this, card)
                bankAccount != null -> bankAccountConfiguration(this, bankAccount)
                device != null -> deviceConfiguration(this, device)
                else -> return@with
            }
        }

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

    private fun copyToClipboardAndToast(message: String?): Boolean {
        message?.let { copyToClipboard(it) }
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun accountConfiguration(navigationView: NavigationView, account: Account) =
        with(navigationView) {
            this.inflateMenu(R.menu.menu_moreoptions_account)
            this.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.Menu_CopyUsername -> copyToClipboardAndToast(account.username)
                    R.id.Menu_CopyEmail -> copyToClipboardAndToast(account.email)
                    R.id.Menu_CopyPass -> copyToClipboardAndToast(account.password)
                    R.id.Menu_VisitSite -> openInBrowser(account.website)
                    R.id.Menu_Delete -> {
                        /* Delete the account */
                        viewModel.delete(account)
                        /*
                        * Send a copy of deleted version back to
                        * main fragment in case user wants to undo
                        */
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            KEY_CREDENTIAL_RESTORE,
                            account
                        )
                    }
                }
                dismiss()
                true
            }
        }

    private fun cardConfiguration(navigationView: NavigationView, card: Card) =
        with(navigationView) {
            this.inflateMenu(R.menu.menu_moreoptions_card)
            this.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.Menu_CopyNumber -> copyToClipboardAndToast(card.number)
                    R.id.Menu_CopyPin -> copyToClipboardAndToast(card.pin)
                    R.id.Menu_CopyBank -> copyToClipboardAndToast(card.bank)
                    R.id.Menu_Delete -> {
                        /* Delete the account */
                        viewModel.delete(card)
                        /*
                        * Send a copy of deleted version back to
                        * main fragment in case user wants to undo
                        */
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            KEY_CREDENTIAL_RESTORE,
                            card
                        )
                    }
                }
                dismiss()
                true
            }
        }

    private fun bankAccountConfiguration(navigationView: NavigationView, bankAccount: BankAccount) =
        with(navigationView) {
            this.inflateMenu(R.menu.menu_moreoptions_bankaccount)
            this.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.Menu_CopyNumber -> copyToClipboardAndToast(bankAccount.accountNumber)
                    R.id.Menu_CopySwift -> copyToClipboardAndToast(bankAccount.swiftCode)
                    R.id.Menu_CopyIban -> copyToClipboardAndToast(bankAccount.iban)
                    R.id.Menu_CopyBank -> copyToClipboardAndToast(bankAccount.bank)
                    R.id.Menu_Delete -> {
                        /* Delete the account */
                        viewModel.delete(bankAccount)
                        /*
                        * Send a copy of deleted version back to
                        * main fragment in case user wants to undo
                        */
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            KEY_CREDENTIAL_RESTORE,
                            bankAccount
                        )
                    }
                }
                dismiss()
                true
            }
        }

    private fun deviceConfiguration(navigationView: NavigationView, device: Device) =
        with(navigationView) {
            this.inflateMenu(R.menu.menu_moreoptions_device)
            this.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.Menu_CopyUsername -> copyToClipboardAndToast(device.username)
                    R.id.Menu_CopyPass -> copyToClipboardAndToast(device.password)
                    R.id.Menu_CopyIp -> copyToClipboardAndToast(device.ipAddress)
                    R.id.Menu_SharePassword -> sharePassword(device)
                    R.id.Menu_Delete -> {
                        /* Delete the account */
                        viewModel.delete(device)
                        /*
                        * Send a copy of deleted version back to
                        * main fragment in case user wants to undo
                        */
                        findNavController().previousBackStackEntry?.savedStateHandle?.set(
                            KEY_CREDENTIAL_RESTORE,
                            device
                        )
                    }
                }
                dismiss()
                true
            }
        }

    private fun openInBrowser(website: String) {
        val intent = LockyUtil.openUrl(website)
        if (isIntentSafeToStart(intent)) startActivity(intent) else toast(
            getString(
                R.string.text_message_alert_intent_none,
                getString(R.string.word_browser_preposition)
            )
        )
    }

    private fun isIntentSafeToStart(intent: Intent) =
        intent.resolveActivity(requireActivity().packageManager) != null

    private fun sharePassword(device: Device) {
        val sendIntent: Intent = LockyUtil.share(
            getString(
                R.string.message_credentials_password_share,
                device.username,
                device.password
            )
        )
        startActivity(Intent.createChooser(sendIntent, null))
    }
}