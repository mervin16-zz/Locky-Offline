package com.th3pl4gu3.locky.ui.main.view.card

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.Card
import com.th3pl4gu3.locky.databinding.FragmentViewCardBinding
import com.th3pl4gu3.locky.ui.main.utils.action
import com.th3pl4gu3.locky.ui.main.utils.copyToClipboard
import com.th3pl4gu3.locky.ui.main.utils.snackbar
import com.th3pl4gu3.locky.ui.main.utils.toast
import com.th3pl4gu3.locky.ui.main.view.CopyClickListener
import com.th3pl4gu3.locky.ui.main.view.CredentialsViewAdapter
import com.th3pl4gu3.locky.ui.main.view.ViewClickListener

class ViewCardFragment : Fragment() {

    private var _binding: FragmentViewCardBinding? = null
    private var _viewModel: ViewCardViewModel? = null
    private lateinit var _card: Card

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Fetch the layout and do the binding
        _binding = FragmentViewCardBinding.inflate(inflater, container, false)
        //Instantiate view model
        _viewModel = ViewModelProvider(this).get(ViewCardViewModel::class.java)

        //Fetch the account clicked on the previous screen
        _card = ViewCardFragmentArgs.fromBundle(requireArguments()).parcelcredcard

        with(_card) {

            //Bind the card to the layout for displaying
            binding.card = this

            //Submit the card details to the recyclerview
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
            R.id.Action_Edit -> {
                navigateToEditScreen()
                true
            }

            R.id.Action_Delete -> {
                deleteCardAndNavigateBackToCardList()
                true
            }
            else -> false
        }
    }

    private fun navigateToEditScreen() {
        findNavController().navigate(
            ViewCardFragmentDirections.actionFragmentViewCardToAddCardFragment()
                .setPARCELCREDCARD(_card)
        )
    }

    private fun deleteCardAndNavigateBackToCardList() {
        with(_card) {
            viewModel.delete(cardID)
            toast(getString(R.string.message_credentials_deleted, entryName))
            findNavController().popBackStack()
        }
    }

    private fun initiateCredentialsFieldList(): CredentialsViewAdapter {
        val credentialsAdapter =
            CredentialsViewAdapter(
                CopyClickListener { data ->
                    copyToClipboardAndToast(data)
                },
                ViewClickListener {
                    snackBarAction()
                })

        binding.RecyclerViewCredentialsField.apply {
            adapter = credentialsAdapter
            setHasFixedSize(true)
        }

        return credentialsAdapter
    }

    private fun snackBarAction() {
        binding.LayoutCredentialView.snackbar(_card.pin) {
            action(getString(R.string.button_snack_action_close)) { dismiss() }
        }
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        requireContext().copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun toast(message: String) = requireContext().toast(message)
}
