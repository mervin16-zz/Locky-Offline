package com.th3pl4gu3.locky.ui.main.view.account

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.Account
import com.th3pl4gu3.locky.databinding.FragmentViewAccountBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.VALUE_EMPTY
import com.th3pl4gu3.locky.ui.main.utils.action
import com.th3pl4gu3.locky.ui.main.utils.copyToClipboard
import com.th3pl4gu3.locky.ui.main.utils.snackbar
import com.th3pl4gu3.locky.ui.main.utils.toast
import com.th3pl4gu3.locky.ui.main.view.CopyClickListener
import com.th3pl4gu3.locky.ui.main.view.CredentialsViewAdapter
import com.th3pl4gu3.locky.ui.main.view.ViewClickListener

class ViewAccountFragment : Fragment() {

    private var _binding: FragmentViewAccountBinding? = null
    private var _viewModel: ViewAccountViewModel? = null
    private lateinit var _account: Account

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Fetch the layout and do the binding
        _binding = FragmentViewAccountBinding.inflate(inflater, container, false)
        //Instantiate view model
        _viewModel = ViewModelProvider(this).get(ViewAccountViewModel::class.java)

        //Fetch the account clicked on the previous screen
        _account = ViewAccountFragmentArgs.fromBundle(requireArguments()).parcelcredaccount

        with(_account) {

            //Bind the account to the layout for displaying
            binding.account = this

            //Submit the account details to the recyclerview
            initiateCredentialsFieldList().submitList(viewModel.fieldList(this))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_credentials_actions, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Action_Duplicate -> {
                /*
                * We set the account id to empty here
                * When the add screen receives it, it wil perceive it as a new account that needs to be
                * added to the database
                */
                navigateToEditScreen(_account.apply {
                    accountID = VALUE_EMPTY
                })
                true
            }

            R.id.Action_Edit -> {
                navigateToEditScreen(_account)
                true
            }

            R.id.Action_Delete -> {
                deleteConfirmationDialog(_account.accountName)
                true
            }
            else -> false
        }
    }

    private fun initiateCredentialsFieldList(): CredentialsViewAdapter {
        val credentialsAdapter =
            CredentialsViewAdapter(
                CopyClickListener { data ->
                    //TODO("Fix Copying issue for password")
                    copyToClipboardAndToast(data)
                },
                ViewClickListener {
                    //TODO("Fix viewing issue")
                    snackBarAction()
                })

        binding.RecyclerViewCredentialsField.apply {
            adapter = credentialsAdapter
            setHasFixedSize(true)
        }

        return credentialsAdapter
    }

    private fun deleteAndNavigateBackToAccountList() {
        with(_account) {
            viewModel.delete(accountID)
            toast(getString(R.string.message_credentials_deleted, accountName))
            findNavController().popBackStack()
        }
    }

    private fun navigateToEditScreen(account: Account) {
        findNavController().navigate(
            ViewAccountFragmentDirections.actionFragmentViewAccountToFragmentAddAccount()
                .setPARCELCREDACCOUNT(account)
        )
    }

    private fun snackBarAction() {
        binding.LayoutCredentialView.snackbar(_account.password) {
            action(getString(R.string.button_snack_action_close)) { dismiss() }
        }
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        requireContext().copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun deleteConfirmationDialog(name: String) =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.text_title_alert_delete, name))
            .setMessage(getString(R.string.text_title_alert_delete_message_account, name))
            .setNegativeButton(R.string.button_action_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.button_action_delete) { _, _ ->
                deleteAndNavigateBackToAccountList()
            }
            .show()

    private fun toast(message: String) = requireContext().toast(message)
}
