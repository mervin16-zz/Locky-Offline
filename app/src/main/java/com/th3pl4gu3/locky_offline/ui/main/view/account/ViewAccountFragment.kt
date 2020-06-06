package com.th3pl4gu3.locky_offline.ui.main.view.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.databinding.FragmentViewAccountBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.*
import com.th3pl4gu3.locky_offline.ui.main.view.CopyClickListener
import com.th3pl4gu3.locky_offline.ui.main.view.CredentialsViewAdapter
import com.th3pl4gu3.locky_offline.ui.main.view.ViewClickListener

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
        binding.lifecycleOwner = this

        //Fetch the account clicked on the previous screen
        _account = ViewAccountFragmentArgs.fromBundle(requireArguments()).parcelcredaccount

        Log.i(
            "OBJECT_RETAIN_BUG",
            "View_Account onCreateView() accountName: ${_account.accountName}"
        )

        with(_account) {

            //Bind the account to the layout for displaying
            binding.account = this

            //Submit the account details to the recyclerview
            initiateCredentialsFieldList().submitList(viewModel.fieldList(this))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Hides the soft keyboard */
        hideSoftInput()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(
            "OBJECT_RETAIN_BUG",
            "View_account onDestroyView() accountName: ${_account.accountName}"
        )
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
                navigateToEditScreen(_account.copy(accountID = generateUniqueID()))
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
                    copyToClipboardAndToast(data)
                },
                ViewClickListener { data ->
                    snackBarAction(data)
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
        navigateTo(
            ViewAccountFragmentDirections.actionFragmentViewAccountToFragmentAddAccount()
                .setPARCELCREDACCOUNT(account)
        )
    }

    private fun snackBarAction(message: String) {
        binding.LayoutCredentialView.snackbar(message) {
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

    private fun hideSoftInput() = requireActivity().hideSoftKeyboard(binding.root)
}
