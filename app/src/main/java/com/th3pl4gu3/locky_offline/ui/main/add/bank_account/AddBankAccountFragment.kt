package com.th3pl4gu3.locky_offline.ui.main.add.bank_account

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
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.databinding.FragmentAddBankAccountBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_BANK_ACCOUNT_LOGO_HEX

class AddBankAccountFragment : Fragment() {

    private var _binding: FragmentAddBankAccountBinding? = null
    private var _viewModel: AddBankAccountViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Binds the UI */
        _binding = FragmentAddBankAccountBinding.inflate(inflater, container, false)
        /* Instantiate the view model */
        _viewModel = ViewModelProvider(this).get(AddBankAccountViewModel::class.java)
        /* Bind view model to layout */
        binding.viewModel = viewModel
        /* Bind lifecycle owner to this */
        binding.lifecycleOwner = this

        /*
        * Fetch the key from argument
        * And set it to view model for fetching
        */
        viewModel.loadBankAccount(
            AddBankAccountFragmentArgs.fromBundle(requireArguments()).keybankaccount,
            AddBankAccountFragmentArgs.fromBundle(requireArguments()).keybankaccountprevious
        )
        /* Returns the root view */
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * Change logo accent click listener
        */
        logoAccentClickListener()

        /**
         * Form validity event
         */
        observeFormValidityEvent()

        /**
         * Form validation error messages events
         */
        observeFormValidationErrorMessagesEvents()

        /**
         * Observe toast event
         */
        observeToastEvent()

        /*
         * Observe if form has errors
         */
        observeIfHasErrors()

        /*
        * Observe back stack entry for
        * accent color changes in logo
        */
        observeBackStackEntryForLogoAccent()
    }

    /*
    * Private functions
    */
    private fun observeToastEvent() {
        viewModel.toastEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toast(it)
                viewModel.doneWithToastEvent()
            }
        })
    }

    private fun observeFormValidationErrorMessagesEvents() {
        with(viewModel) {
            nameErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.AccountName.error = it
            })

            numberErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.AccountNumber.error = it
            })

            bankErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.AccountBank.error = it
            })

            ownerErrorMessage.observe(viewLifecycleOwner, Observer {
                binding.AccountOwner.error = it
            })
        }
    }

    private fun observeIfHasErrors() {
        viewModel.hasErrors.observe(viewLifecycleOwner, Observer {
            if (it) {
                scrollToTop()
                viewModel.resetErrorsFlag()
            }
        })
    }

    private fun observeFormValidityEvent() {
        viewModel.formValidity.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToastAndNavigateToBankAccountList(it)
            }
        })
    }

    private fun observeBackStackEntryForLogoAccent() {
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.Fragment_Add_BankAccount)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(KEY_BANK_ACCOUNT_LOGO_HEX)
            ) {
                /*
                * Update the accent color
                */
                viewModel.accent = navBackStackEntry.savedStateHandle.get<String>(KEY_BANK_ACCOUNT_LOGO_HEX)!!

                navBackStackEntry.savedStateHandle.remove<String>(KEY_BANK_ACCOUNT_LOGO_HEX)
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private fun logoAccentClickListener() {
        binding.AccountLogoEdit.setOnClickListener {
            navigateTo(AddBankAccountFragmentDirections.actionFragmentAddBankAccountToFragmentBottomDialogLogoBankAccount()
                .setHEXCURRENT(viewModel.accent))
        }
    }

    private fun showToastAndNavigateToBankAccountList(toastMessage: String) {
        toast(toastMessage)
        navigateTo(AddBankAccountFragmentDirections.actionFragmentAddBankAccountToFragmentBankAccount())
    }

    private fun scrollToTop() = with(binding.LayoutParentAddBankAccount) {
        fling(0)
        smoothScrollTo(0, 0)
    }
}
