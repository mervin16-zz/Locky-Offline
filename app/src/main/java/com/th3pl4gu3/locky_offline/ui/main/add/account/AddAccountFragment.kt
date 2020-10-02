package com.th3pl4gu3.locky_offline.ui.main.add.account

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
import com.th3pl4gu3.locky_offline.databinding.FragmentAddAccountBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_ACCOUNT_LOGO

class AddAccountFragment : Fragment() {

    private var _binding: FragmentAddAccountBinding? = null
    private var _viewModel: AddAccountViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        /* Binds the UI */
        _binding = FragmentAddAccountBinding.inflate(inflater, container, false)
        /* Instantiate the view model */
        _viewModel = ViewModelProvider(this).get(AddAccountViewModel::class.java)

        /* Bind view model to layout */
        binding.viewModel = viewModel
        /* Bind lifecycle owner to this */
        binding.lifecycleOwner = this

        /*
        * Fetch the key from argument
        * And set it to view model for fetching
        */
        viewModel.loadAccount(
            AddAccountFragmentArgs.fromBundle(requireArguments()).keyaccount,
            AddAccountFragmentArgs.fromBundle(requireArguments()).keyaccountprevious
        )

        /* Returns the root view */
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //On click listener for logo search sheet
        listenerLogoClick()

        //Toast event
        observeToastEvent()

        //Form validity
        observeFormValidity()

        //Observe form error message events
        observeFormErrorMessageEvents()

        //Observe back stack entry for logo result
        observeBackStackEntryForLogoResult()

        //Observe if form has errors
        observeIfHasErrors()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun observeBackStackEntryForLogoResult() {
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.Fragment_Add_Account)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(KEY_ACCOUNT_LOGO)
            ) {
                /*
                * Update the logo
                */
                viewModel.logoUrl =
                    navBackStackEntry.savedStateHandle.get<String>(KEY_ACCOUNT_LOGO)!!

                navBackStackEntry.savedStateHandle.remove<String>(KEY_ACCOUNT_LOGO)
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

    private fun observeIfHasErrors() = viewModel.hasErrors.observe(viewLifecycleOwner, Observer {
        if (it) {
            scrollToTop()
            viewModel.resetErrorsFlag()
        }
    })

    private fun observeFormErrorMessageEvents() = with(viewModel) {
        nameErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.AccountName.error = it
        })

        passwordErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.AccountPassword.error = it
        })

        emailErrorMessage.observe(viewLifecycleOwner, Observer {
            binding.AccountEmail.error = it
        })
    }

    private fun observeFormValidity() =
        viewModel.formValidity.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToastAndNavigateToAccountList(it)
            }
        })

    private fun observeToastEvent() = with(viewModel) {
        toastEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                toast(it)
                doneWithToastEvent()
            }
        })
    }

    private fun listenerLogoClick() = binding.AccountLogoEdit.setOnClickListener {
        navigateTo(AddAccountFragmentDirections.actionFragmentAddAccountToFragmentBottomDialogLogoAccount())
    }

    private fun scrollToTop() = with(binding.LayoutParentAddAccount) {
        fling(0)
        smoothScrollTo(0, 0)
    }

    private fun showToastAndNavigateToAccountList(toastMessage: String) {
        toast(toastMessage)
        navigateTo(AddAccountFragmentDirections.actionFragmentAddAccountToFragmentAccount())
    }
}
