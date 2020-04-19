package com.th3pl4gu3.locky.ui.main.view.card

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.databinding.FragmentViewCardBinding
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.view.*

class ViewCardFragment : Fragment() {

    private var _binding: FragmentViewCardBinding? = null
    private lateinit var _viewModel: ViewCardViewModel
    private lateinit var _card: Card
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewCardBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(ViewCardViewModel::class.java)

        _card = ViewCardFragmentArgs.fromBundle(
            requireArguments()
        ).parcelcredcard

        binding.card = _card

        initiateCredentialsFieldList().submitList(_viewModel.fieldList(_card))

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
                    ViewCardFragmentDirections.actionViewCardFragmentToFragmentAddCard()
                        .setPARCELCREDCARD(_card)
                )
                true
            }

            R.id.Action_Delete -> {
                //TODO: Add database code to delete account here
                toast(getString(R.string.message_credentials_deleted, _card.name))
                findNavController().navigate(ViewCardFragmentDirections.actionViewCardFragmentToFragmentCard())
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
                    binding.LayoutCredentialView.snackbar(_card.pin.toString()) {
                        action(getString(R.string.button_snack_action_close)) { dismiss() }
                    }
                })

        binding.RecyclerViewCredentialsField.adapter = credentialsAdapter

        return credentialsAdapter
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        requireContext().copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun toast(message: String) = requireContext().toast(message)
}
