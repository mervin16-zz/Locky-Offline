package com.th3pl4gu3.locky_offline.ui.main.view.account

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentViewAccountBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.*
import com.th3pl4gu3.locky_offline.ui.main.view.CopyClickListener
import com.th3pl4gu3.locky_offline.ui.main.view.CredentialsViewAdapter
import com.th3pl4gu3.locky_offline.ui.main.view.ViewClickListener

class ViewAccountFragment : Fragment() {

    private var _binding: FragmentViewAccountBinding? = null
    private var _viewModel: ViewAccountViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Binds the UI */
        _binding = FragmentViewAccountBinding.inflate(inflater, container, false)
        /* Instantiate the view model */
        _viewModel = ViewModelProvider(this).get(ViewAccountViewModel::class.java)
        /* Bind lifecycle owner to this */
        binding.lifecycleOwner = this

        /*
        * Fetch the account object from argument
        * Then bind account object to layout
        */
        binding.account = ViewAccountFragmentArgs.fromBundle(requireArguments()).accountToVIEW

        /* Returns the root view */
        return binding.root
    }

    /*
    * Overridden functions
    */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Hides the soft keyboard */
        hideSoftKeyboard(binding.root)

        /* Load user data */
        subscribeUi()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_credentials_actions, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.Action_Duplicate -> {
                navigateTo(
                    ViewAccountFragmentDirections.actionGlobalFragmentAddAccount()
                        .setKEYACCOUNT(0).setKEYACCOUNTPREVIOUS(binding.account!!.id)
                )
                true
            }
            R.id.Action_Edit -> {
                navigateTo(
                    ViewAccountFragmentDirections.actionGlobalFragmentAddAccount()
                        .setKEYACCOUNT(
                            binding.account!!.id
                        )
                )
                true
            }
            R.id.Action_Delete -> {
                deleteConfirmationDialog(binding.account!!.entryName)
                true
            }
            else -> false
        }


    /*
    * Private functions
    */
    private fun subscribeUi() {
        val adapter = CredentialsViewAdapter(
            CopyClickListener {
                copyToClipboardAndToast(it)
            },
            ViewClickListener {
                snackBarAction(it)
            }
        )

        binding.RecyclerViewCredentialsField.apply {
            /*
            * State that layout size will not change for better performance
            */
            setHasFixedSize(true)

            /* Bind the layout manager */
            layoutManager = LinearLayoutManager(requireContext())

            /* Bind the adapter */
            this.adapter = adapter
        }

        /* Submits the list for displaying */
        adapter.submitList(viewModel.fieldList(binding.account!!))
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

    private fun deleteAndNavigateBackToAccountList() {
        with(binding.account!!) {
            viewModel.delete(id)
            toast(getString(R.string.message_credentials_deleted, entryName))
            findNavController().popBackStack()
        }
    }

    private fun snackBarAction(message: String) {
        binding.LayoutCredentialView.snackbar(message) {
            action(getString(R.string.button_snack_action_close)) { dismiss() }
        }
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }
}
