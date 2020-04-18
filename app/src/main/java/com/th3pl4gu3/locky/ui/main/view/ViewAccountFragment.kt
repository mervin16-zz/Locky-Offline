package com.th3pl4gu3.locky.ui.main.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.databinding.FragmentViewAccountBinding
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.PLACEHOLDER_DATA_NONE

class ViewAccountFragment : Fragment() {

    private var _binding: FragmentViewAccountBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewAccountBinding.inflate(inflater, container, false)

        val account = ViewAccountFragmentArgs.fromBundle(requireArguments()).parcelcredaccount

        binding.account = account

        initiateCredentialsFieldList().submitList(fieldList(account))

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

    private fun copyToClipboardAndToast(message: String): Boolean {
        requireContext().copyToClipboard(message)
        requireContext().toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun fieldList(account: Account): ArrayList<CredentialsField> =
        ArrayList<CredentialsField>().apply {
            add(CredentialsField(getString(R.string.field_account_username), if(account.username.isEmpty()) PLACEHOLDER_DATA_NONE else account.username, isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_account_email), if(account.email.isEmpty()) PLACEHOLDER_DATA_NONE else account.email, isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_account_password), account.password, View.VISIBLE, View.VISIBLE))
            add(CredentialsField(getString(R.string.field_account_website), if(account.website.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.website!!, isCopyable = View.VISIBLE))
            add(CredentialsField(getString(R.string.field_account_2faauth), if(account.twoFA.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.twoFA!!))
            add(CredentialsField(getString(R.string.field_account_2fakeys), if(account.twoFASecretKeys.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.twoFASecretKeys!!))
            add(CredentialsField(getString(R.string.field_account_additional), if(account.additionalInfo.isNullOrEmpty()) PLACEHOLDER_DATA_NONE else account.additionalInfo!!))
        }
}
