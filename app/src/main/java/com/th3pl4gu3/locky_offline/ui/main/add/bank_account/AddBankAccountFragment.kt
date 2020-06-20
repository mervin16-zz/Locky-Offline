package com.th3pl4gu3.locky_offline.ui.main.add.bank_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.th3pl4gu3.locky_offline.databinding.FragmentAddBankAccountBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.toast

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

        /* Observe if form has errors*/
        observeIfHasErrors()
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

    private fun showToastAndNavigateToBankAccountList(toastMessage: String) {
        toast(toastMessage)
        navigateTo(AddBankAccountFragmentDirections.actionFragmentAddBankAccountToFragmentBankAccount())
    }

    private fun scrollToTop() {
        getParentScrollView().fling(0)
        getParentScrollView().smoothScrollTo(0, 0)
    }

    private fun getParentScrollView() = binding.root.parent.parent as NestedScrollView
}
