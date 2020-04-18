package com.th3pl4gu3.locky.ui.main.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.databinding.FragmentViewCardBinding
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.PLACEHOLDER_DATA_NONE

class ViewCardFragment : Fragment() {

    private var _binding: FragmentViewCardBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewCardBinding.inflate(inflater, container, false)

        val card = ViewCardFragmentArgs.fromBundle(requireArguments()).parcelcredcard

        binding.card = card

        initiateCredentialsFieldList().submitList(fieldList(card))

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

    private fun initiateCredentialsFieldList(): CredentialsViewAdapter {
        val credentialsAdapter = CredentialsViewAdapter(
            CopyClickListener { data ->
                copyToClipboardAndToast(data)
            },
            ViewClickListener { data ->
                binding.LayoutCredentialView.snackbar(data) {
                    action(getString(R.string.button_snack_action_close)) { dismiss() }
                }
            })

        binding.RecyclerViewCredentialsField.adapter = credentialsAdapter

        return credentialsAdapter
    }

    private fun fieldList(card: Card): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(CredentialsField(getString(R.string.field_card_name), if(card.name.isEmpty()) PLACEHOLDER_DATA_NONE else card.name, isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_card_bank), if(card.bank.isEmpty()) PLACEHOLDER_DATA_NONE else card.bank, isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_card_pin), card.pin.toString(), View.VISIBLE, View.VISIBLE))
            add(CredentialsField(getString(R.string.field_card_date_issued), card.issuedDate.toFormattedString(), isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_card_date_expiry), card.expiryDate.toFormattedString(), isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_card_additional), if(card.additionalInfo.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else card.additionalInfo!!))
        }

    private fun copyToClipboardAndToast(message: String): Boolean {
        requireContext().copyToClipboard(message)
        requireContext().toast(getString(R.string.message_copy_successful))
        return true
    }
}
