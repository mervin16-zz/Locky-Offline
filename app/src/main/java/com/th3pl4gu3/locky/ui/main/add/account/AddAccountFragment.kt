package com.th3pl4gu3.locky.ui.main.add.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.databinding.FragmentAddAccountBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_ACCOUNT_LOGO
import com.th3pl4gu3.locky.ui.main.utils.toast

class AddAccountFragment : Fragment() {

    private var _binding: FragmentAddAccountBinding? = null
    private lateinit var _viewModel: AddAccountViewModel
    private lateinit var _account: Account

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAccountBinding.inflate(inflater, container, false)

        _viewModel = ViewModelProvider(this).get(AddAccountViewModel::class.java)

        //Bind view model and lifecycle owner
        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        //Fetch account if exists
        _account =
            AddAccountFragmentArgs.fromBundle(requireArguments()).parcelcredaccount ?: Account()
        _viewModel.setAccount(_account)

        binding.ButtonSave.setOnClickListener {
            _viewModel.isFormValid(
                _account.apply {
                    name = binding.AccountName.editText?.text.toString()
                    username = binding.AccountUsername.editText?.text.toString()
                    email = binding.AccountEmail.editText?.text.toString()
                    password = binding.AccountPassword.editText?.text.toString()
                    website = binding.AccountWebsite.editText?.text.toString()
                    additionalInfo = binding.AccountComments.editText?.text.toString()
                    twoFA = binding.Account2FAEnabled.editText?.text.toString()
                    twoFASecretKeys = binding.Account2FAKeys.editText?.text.toString()
                }
            )
        }

        /** Other Observations**/
        _viewModel.toastEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toast(it)
                _viewModel.doneWithToastEvent()
            }
        })

        /** Form Validation Observations**/
        _viewModel.nameErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.AccountName.error = it
        })

        _viewModel.usernameErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.AccountUsername.error = it
        })

        _viewModel.emailErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.AccountEmail.error = it
        })

        _viewModel.passwordErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.AccountPassword.error = it
        })

        _viewModel.isFormValid.observe(viewLifecycleOwner, Observer {
            if (it) {
                //TODO: Add _account to database here
                toast(getString(R.string.message_credentials_created, _account.name))
                findNavController().navigate(AddAccountFragmentDirections.actionAddAccountFragmentToFragmentAccount())
            }
        })

        val navBackStackEntry = findNavController().currentBackStackEntry!!
        navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains(
                    KEY_ACCOUNT_LOGO
                )
            ) {
                _viewModel.setAccountLogo(
                    navBackStackEntry.savedStateHandle.get<String>(
                        KEY_ACCOUNT_LOGO
                    )!!
                )
                navBackStackEntry.savedStateHandle.remove<String>(KEY_ACCOUNT_LOGO)
            }
        })

        binding.AccountLogo.setOnClickListener {
            findNavController().navigate(AddAccountFragmentDirections.actionFragmentAddAccountToLogoBottomSheetFragment())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toast(message: String) = requireContext().toast(message)
}
