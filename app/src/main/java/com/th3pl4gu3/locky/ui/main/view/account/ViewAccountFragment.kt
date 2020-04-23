package com.th3pl4gu3.locky.ui.main.view.account

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.databinding.FragmentViewAccountBinding
import com.th3pl4gu3.locky.ui.main.utils.action
import com.th3pl4gu3.locky.ui.main.utils.copyToClipboard
import com.th3pl4gu3.locky.ui.main.utils.snackbar
import com.th3pl4gu3.locky.ui.main.utils.toast
import com.th3pl4gu3.locky.ui.main.view.CopyClickListener
import com.th3pl4gu3.locky.ui.main.view.CredentialsViewAdapter
import com.th3pl4gu3.locky.ui.main.view.ViewClickListener

class ViewAccountFragment : Fragment() {

    private var _binding: FragmentViewAccountBinding? = null
    private lateinit var _account: Account
    private lateinit var _viewModel: ViewAccountViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewAccountBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(ViewAccountViewModel::class.java)

        _account = ViewAccountFragmentArgs.fromBundle(requireArguments()).parcelcredaccount

        binding.account = _account

        initiateCredentialsFieldList().submitList(_viewModel.fieldList(_account))

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
            R.id.Action_Edit -> {
                findNavController().navigate(
                    ViewAccountFragmentDirections.actionFragmentViewAccountToFragmentAddAccount()
                        .setPARCELCREDACCOUNT(_account)
                )
                true
            }

            R.id.Action_Delete -> {
                //TODO: Add database code to delete account here
                toast(getString(R.string.message_credentials_deleted, _account.name))
                findNavController().popBackStack()
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
                    binding.LayoutCredentialView.snackbar(_account.password) {
                        action(getString(R.string.button_snack_action_close)) { dismiss() }
                    }
                })

        binding.RecyclerViewCredentialsField.apply {
            adapter = credentialsAdapter
            setHasFixedSize(true)
        }

        return credentialsAdapter
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        requireContext().copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun toast(message: String) = requireContext().toast(message)
}
