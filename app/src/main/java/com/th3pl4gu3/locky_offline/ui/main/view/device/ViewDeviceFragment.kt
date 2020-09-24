package com.th3pl4gu3.locky_offline.ui.main.view.device

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Device
import com.th3pl4gu3.locky_offline.databinding.FragmentViewCredentialsBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.*
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.LockyUtil
import com.th3pl4gu3.locky_offline.ui.main.view.CredentialViewModel
import com.th3pl4gu3.locky_offline.ui.main.view.CredentialsViewAdapter
import com.th3pl4gu3.locky_offline.ui.main.view.ViewCredentialListener
import kotlinx.coroutines.launch

class ViewDeviceFragment : Fragment(), ViewCredentialListener {

    private var _binding: FragmentViewCredentialsBinding? = null
    private var _viewModel: CredentialViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Binds the UI
        _binding = FragmentViewCredentialsBinding.inflate(inflater, container, false)
        // Instantiate the View model
        _viewModel = ViewModelProvider(this).get(CredentialViewModel::class.java)
        // Bind view model to layout
        binding.viewModel = viewModel
        // Bind lifecycle owner to this
        binding.lifecycleOwner = this
        // Fetch the bank account object from argument and to layout
        binding.credential = ViewDeviceFragmentArgs.fromBundle(requireArguments()).deviceToVIEW
        // Pass the credential object to view model to update messages
        viewModel.updateMessageType(binding.credential!!)
        // Return the root view
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Hides the soft keyboard */
        hideSoftKeyboard(binding.root)

        /* Load user data */
        subscribeUi()
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
                navigateTo(
                    ViewDeviceFragmentDirections.actionGlobalFragmentAddDevice()
                        .setKEYDEVICE(0).setKEYDEVICEPREVIOUS(binding.credential!!.id)
                )
                true
            }

            R.id.Action_Edit -> {
                navigateTo(
                    ViewDeviceFragmentDirections.actionGlobalFragmentAddDevice()
                        .setKEYDEVICE(
                            binding.credential!!.id
                        )
                )
                true
            }

            R.id.Action_Delete -> {
                deleteConfirmationDialog(binding.credential!!.entryName)
                true
            }
            else -> false
        }
    }

    override fun onCopyClicked(data: String) {
        copyToClipboardAndToast(data)
    }

    override fun onViewClicked(data: String) {
        showPasswordDialog(data)
    }

    override fun onShareClicked(data: String) {
        sharePassword(data)
    }


    /*
    * Private Functions
    */
    private fun deleteCardAndNavigateBackToDeviceList() {
        with(binding.credential!!) {
            viewModel.delete(this)
            toast(getString(R.string.message_credentials_deleted, entryName))
            findNavController().popBackStack()
        }
    }

    private fun subscribeUi() {
        val adapter = CredentialsViewAdapter(this)

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

        lifecycleScope.launch {
            adapter.submitList(viewModel.deviceFields(binding.credential!! as Device))
        }
    }

    private fun showPasswordDialog(password: String) =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                getString(
                    R.string.text_title_alert_showPin
                )
            )
            .setMessage(
                password.setColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )
            )
            .setPositiveButton(R.string.button_action_close) { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    private fun copyToClipboardAndToast(message: String): Boolean {
        copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun deleteConfirmationDialog(name: String) =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.text_title_alert_delete, name))
            .setMessage(getString(R.string.text_title_alert_delete_message, name))
            .setNegativeButton(R.string.button_action_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.button_action_delete) { _, _ ->
                deleteCardAndNavigateBackToDeviceList()
            }
            .show()

    private fun sharePassword(password: String) {
        val sendIntent: Intent = LockyUtil.share(
            getString(
                R.string.message_credentials_password_share,
                (binding.credential!! as Device).username,
                password
            )
        )
        startActivity(Intent.createChooser(sendIntent, null))
    }
}
