package com.th3pl4gu3.locky_offline.ui.main.add.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.databinding.FragmentAddAccountBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_ACCOUNT_LOGO
import com.th3pl4gu3.locky_offline.ui.main.utils.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.toast

class AddAccountFragment : Fragment() {

    private var _binding: FragmentAddAccountBinding? = null
    private var _viewModel: AddAccountViewModel? = null
    private var _unEditedAccount: Account? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

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

        val account = AddAccountFragmentArgs.fromBundle(requireArguments()).parcelcredaccount
        _unEditedAccount = account?.copy()
        //Fetch account if exists
        viewModel.setAccount(account)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (_unEditedAccount != null) {
                viewModel.resetChanges(_unEditedAccount!!)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeBackStackEntryForLogoResult() {
        val navBackStackEntry = findNavController().currentBackStackEntry!!
        navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains(
                    KEY_ACCOUNT_LOGO
                )
            ) {
                //Set the logo url in the view model
                viewModel.logoUrl =
                    navBackStackEntry.savedStateHandle.get<String>(KEY_ACCOUNT_LOGO)!!

                navBackStackEntry.savedStateHandle.remove<String>(KEY_ACCOUNT_LOGO)
            }
        })
    }

    private fun observeIfHasErrors() {
        viewModel.hasErrors.observe(viewLifecycleOwner, Observer {
            if (it) {
                scrollToTop()
                viewModel.resetErrorsFlag()
            }
        })
    }

    private fun observeFormErrorMessageEvents() {
        with(viewModel) {
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
    }

    private fun observeFormValidity() {
        viewModel.formValidity.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToastAndNavigateToAccountList(it)
            }
        })
    }

    private fun observeToastEvent() {
        with(viewModel) {
            toastEvent.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    toast(it)
                    doneWithToastEvent()
                }
            })
        }
    }

    private fun listenerLogoClick() {
        binding.AccountLogoEdit.setOnClickListener {
            navigateToLogoSearch()
        }
    }

    private fun scrollToTop() {
        getParentScrollView().fling(0)
        getParentScrollView().smoothScrollTo(0, 0)
    }

    private fun getParentScrollView() = binding.root.parent.parent as NestedScrollView

    private fun showToastAndNavigateToAccountList(toastMessage: String) {
        toast(toastMessage)
        navigateTo(AddAccountFragmentDirections.actionFragmentAddAccountToFragmentAccount())
    }

    private fun navigateToLogoSearch() {
        navigateTo(AddAccountFragmentDirections.actionFragmentAddAccountToBottomSheetFragmentAccountLogo())
    }
}
