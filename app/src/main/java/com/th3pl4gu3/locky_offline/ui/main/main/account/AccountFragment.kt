package com.th3pl4gu3.locky_offline.ui.main.main.account

import android.os.Bundle
import android.os.SystemClock
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.AccountSort
import com.th3pl4gu3.locky_offline.databinding.FragmentAccountBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.*
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.Companion.KEY_ACCOUNTS_SORT


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private var _viewModel: AccountViewModel? = null
    private var _lastClickTime: Long = 0

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    companion object {
        const val TAG = "ACCOUNT_FRAGMENT_DEBUG"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        // Fetch view model
        _viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        //Bind view model to layout
        binding.viewModel = _viewModel
        // Bind lifecycle owner
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Hides the soft keyboard */
        hideSoftInput()

        /* Observe snack bar events */
        observeSnackBarEvent()

        /* Observe the account list changes */
        observeAccounts()

        /* Observe back stack entry result after navigating from sort sheet */
        observeBackStackEntryForSortSheet()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Toolbar_Filter -> {
                navigateToSort()
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    * My Functions
    */
    private fun observeBackStackEntryForSortSheet() {
        val navController = findNavController()
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        val navBackStackEntry = navController.getBackStackEntry(R.id.Fragment_Account)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(KEY_ACCOUNTS_SORT)
            ) {

                viewModel.sortChange(
                    navBackStackEntry.savedStateHandle.get<AccountSort>(
                        KEY_ACCOUNTS_SORT
                    )!!
                )

                navBackStackEntry.savedStateHandle.remove<AccountSort>(KEY_ACCOUNTS_SORT)
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

    private fun observeSnackBarEvent() {
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                snackBarAction(it)
            }
        })
    }

    private fun observeAccounts() {
        with(viewModel) {
            accounts.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    //set loading flag to hide loading animation
                    doneLoading()

                    //Alternate visibility for account list and empty view
                    alternateAccountListVisibility(it.size)

                    //Submit the cards
                    initiateAccounts().submitList(it)
                }
            })
        }
    }

    private fun initiateAccounts(): AccountAdapter {
        val adapter = AccountAdapter(
            AccountClickListener {
                navigateToSelectedAccount(it)
            },
            AccountOptionsClickListener { view, card ->
                view.apply {
                    isEnabled = false
                }
                createPopupMenu(view, card)
            })

        binding.RecyclerViewAccount.apply {
            this.adapter = adapter
            setHasFixedSize(true)
        }

        return adapter
    }

    private fun createPopupMenu(view: View, account: Account) {
        requireContext().createPopUpMenu(
            view,
            R.menu.menu_moreoptions_account,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyUsername -> copyToClipboardAndToast(account.username)
                    R.id.Menu_CopyPass -> copyToClipboardAndToast(account.password)
                    R.id.Menu_ShowPass -> triggerSnackBarEvent(account.password)
                    else -> false
                }
            }, PopupMenu.OnDismissListener {
                view.apply {
                    isEnabled = true
                }
            })
    }

    private fun navigateToSort() {
        if (SystemClock.elapsedRealtime() - _lastClickTime >= 800) {
            _lastClickTime = SystemClock.elapsedRealtime()
            navigateTo(AccountFragmentDirections.actionFragmentAccountToBottomSheetFragmentAccountFilter())
        }
    }

    private fun navigateToSelectedAccount(account: Account) {
        navigateTo(
            AccountFragmentDirections.actionFragmentAccountToFragmentViewAccount(
                account
            )
        )
    }

    private fun snackBarAction(message: String) {
        binding.LayoutFragmentAccount.snackbar(message) {
            action(getString(R.string.button_snack_action_close)) { dismiss() }
        }
        viewModel.doneShowingSnackBar()
    }

    private fun triggerSnackBarEvent(message: String): Boolean {
        viewModel.setSnackBarMessage(message)
        return true
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        requireContext().copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun hideSoftInput() = requireActivity().hideSoftKeyboard(binding.root)

    private fun toast(message: String) = requireContext().toast(message)
}
