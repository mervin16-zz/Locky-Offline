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
import androidx.recyclerview.widget.LinearLayoutManager
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.AccountSort
import com.th3pl4gu3.locky_offline.databinding.FragmentAccountBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.*
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_ACCOUNTS_SORT

class AccountFragment : Fragment() {

    /*
    * Private variables
    */
    private var _binding: FragmentAccountBinding? = null
    private var _viewModel: AccountViewModel? = null
    private var _lastClickTime: Long = 0

    /*
    * Private properties
    */
    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    /*
    * Companion object
    */
    companion object {
        const val TAG = "ACCOUNT_FRAGMENT_DEBUG"
    }

    /*
    * Overridden methods
    */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* Binds the UI */
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        /* Instantiate the view model */
        _viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        /* Bind the view model to the layout */
        binding.viewModel = viewModel
        /* Bind lifecycle owner to this */
        binding.lifecycleOwner = this
        /* Returns the root view */
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Hides the soft keyboard */
        hideSoftKeyboard(binding.root)

        /* Observe the account event changes */
        observeAccounts()

        /* Observe the snack bar event changes*/
        observeSnackBarEvent()

        /* Observe back stack entry for sort changes */
        observeBackStackEntryForSortSheet()
    }

    /*
    * Explicit private functions
    */
    private fun observeSnackBarEvent() {
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                snackBarAction(it)
            }
        })
    }

    private fun observeAccounts() {
        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                /*
                 * If accounts is not null
                 * Update the ui and
                 * Load recyclerview
                 */
                updateUI(it.size)

                subscribeAccounts(it)
            }
        })
    }

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

    private fun subscribeAccounts(accounts: List<Account>) {
        val adapter = AccountAdapter(
            /* The click listener to handle account on clicks */
            AccountClickListener { view, account ->
                navigateTo(
                    AccountFragmentDirections.actionFragmentAccountToFragmentViewAccount(
                        account
                    )
                )
            },
            /* The click listener to handle popup menu for each accounts */
            AccountOptionsClickListener { view, account ->
                view.apply {
                    isEnabled = false
                }
                createPopupMenu(view, account)
            },
            false
        )

        binding.RecyclerViewAccount.apply {
            /*
            * State that layout size will not change for better performance
            */
            setHasFixedSize(true)

            /* Bind the layout manager */
            layoutManager = LinearLayoutManager(requireContext())

            /* Bind the adapter */
            this.adapter = adapter
        }

        /* Submits the list for displaying */
        adapter.submitList(accounts)
    }

    private fun updateUI(listSize: Int) {
        /*
        * Hide the loading animation
        * Alternate visibility between
        * Recyclerview & Empty View
        */
        viewModel.doneLoading(listSize)
    }

    private fun createPopupMenu(view: View, account: Account) {
        createPopUpMenu(
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
            navigateTo(AccountFragmentDirections.actionFragmentAccountToFragmentBottomDialogFilterAccount())
        }
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
        copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }
}
