package com.th3pl4gu3.locky_offline.ui.main.view.card

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentViewCardBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.*
import com.th3pl4gu3.locky_offline.ui.main.view.CopyClickListener
import com.th3pl4gu3.locky_offline.ui.main.view.CredentialsViewAdapter
import com.th3pl4gu3.locky_offline.ui.main.view.ViewClickListener

class ViewCardFragment : Fragment() {

    private var _binding: FragmentViewCardBinding? = null
    private var _viewModel: ViewCardViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*Fetch the layout and do the binding*/
        _binding = FragmentViewCardBinding.inflate(inflater, container, false)
        /*Instantiate view model*/
        _viewModel = ViewModelProvider(this).get(ViewCardViewModel::class.java)
        /* Bind view model to layout */
        binding.viewModel = viewModel
        /*Bind lifecycle owner to this*/
        binding.lifecycleOwner = this
        /*Fetch the card clicked on the previous screen*/
        val card = ViewCardFragmentArgs.fromBundle(requireArguments()).cardToVIEW
        /* Assign card to binding */
        binding.card = card
        /* We pass the card object to view model to update messages */
        viewModel.updateMessageType(binding.card!!)
        /* Return root view */
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
                    ViewCardFragmentDirections.actionGlobalFragmentAddCard()
                        .setKEYCARD(0).setKEYCARDPREVIOUS(binding.card!!.id)
                )
                true
            }

            R.id.Action_Edit -> {
                navigateTo(
                    ViewCardFragmentDirections.actionGlobalFragmentAddCard()
                        .setKEYCARD(
                            binding.card!!.id
                        )
                )
                true
            }

            R.id.Action_Delete -> {
                deleteConfirmationDialog(binding.card!!.entryName)
                true
            }
            else -> false
        }
    }

    private fun deleteCardAndNavigateBackToCardList() {
        with(binding.card!!) {
            viewModel.delete(id)
            toast(getString(R.string.message_credentials_deleted, entryName))
            findNavController().popBackStack()
        }
    }

    private fun subscribeUi() {
        val adapter =
            CredentialsViewAdapter(
                copyClickListener = CopyClickListener { data ->
                    copyToClipboardAndToast(data)
                },
                viewClickListener = ViewClickListener { data ->
                    showPasswordDialog(data)
                })

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

        adapter.submitList(viewModel.fieldList(binding.card!!))
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
                deleteCardAndNavigateBackToCardList()
            }
            .show()
}
