package com.th3pl4gu3.locky_offline.ui.main.main.bank_account

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
import com.google.android.material.transition.MaterialSharedAxis
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.credentials.BankAccount
import com.th3pl4gu3.locky_offline.core.main.tuning.BankAccountSort
import com.th3pl4gu3.locky_offline.databinding.FragmentBankAccountBinding
import com.th3pl4gu3.locky_offline.ui.main.main.ClickListener
import com.th3pl4gu3.locky_offline.ui.main.main.CredentialsAdapter
import com.th3pl4gu3.locky_offline.ui.main.main.OptionsClickListener
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_BANK_ACCOUNTS_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.VALUE_EMPTY
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.*

class BankAccountFragment : Fragment() {

    /*
    * Private variables
    */
    private var _binding: FragmentBankAccountBinding? = null
    private var _viewModel: BankAccountViewModel? = null
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
        const val TAG = "BANK_ACCOUNT_FRAGMENT_DEBUG"
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
        _binding = FragmentBankAccountBinding.inflate(inflater, container, false)
        /* Instantiate the view model */
        _viewModel = ViewModelProvider(this).get(BankAccountViewModel::class.java)
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

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
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

        /* Observe bank accounts */
        observeAccounts()

        /* Observe back stack entry for sort changes */
        observeBackStackEntryForSortSheet()
    }

    /*
    * Explicit private functions
    */
    private fun observeAccounts() {
        viewModel.bankAccounts.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                /*
                 * If bank accounts is not null
                 * Update the ui and
                 * Load recyclerview
                 */
                updateUI(it.size)

                subscribeBankAccounts(it)
            }
        })
    }

    private fun observeBackStackEntryForSortSheet() {
        val navController = findNavController()
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        val navBackStackEntry = navController.getBackStackEntry(R.id.Fragment_Bank_Account)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(KEY_BANK_ACCOUNTS_SORT)
            ) {

                viewModel.sortChange(
                    navBackStackEntry.savedStateHandle.get<BankAccountSort>(
                        KEY_BANK_ACCOUNTS_SORT
                    )!!
                )

                navBackStackEntry.savedStateHandle.remove<BankAccountSort>(KEY_BANK_ACCOUNTS_SORT)
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

    private fun subscribeBankAccounts(bankAccounts: List<BankAccount>) {
        val adapter = CredentialsAdapter(
            /* The click listener to handle account on clicks */
            ClickListener {
                navigateTo(
                    BankAccountFragmentDirections.actionFragmentBankAccountToFragmentViewBankAccount(
                        it as BankAccount
                    )
                )
            },
            /* The click listener to handle popup menu for each accounts */
            OptionsClickListener { view, credential ->
                view.apply {
                    isEnabled = false
                }
                createPopupMenu(view, credential as BankAccount)
            },
            false
        )

        binding.RecyclerViewBankAccount.apply {
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
        adapter.submitList(bankAccounts)
    }

    private fun updateUI(listSize: Int) {
        /*
        * Hide the loading animation
        * Alternate visibility between
        * Recyclerview & Empty View
        */
        viewModel.doneLoading(listSize)
    }

    private fun createPopupMenu(view: View, account: BankAccount) {
        createPopUpMenu(
            view,
            R.menu.menu_moreoptions_bankaccount,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyNumber -> copyToClipboardAndToast(account.accountNumber)
                    R.id.Menu_CopyIban -> copyToClipboardAndToast(account.iban)
                    R.id.Menu_CopySwift -> copyToClipboardAndToast(account.swiftCode)
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
            navigateTo(BankAccountFragmentDirections.actionFragmentBankAccountToFragmentBottomDialogFilterBankAccount())
        }
    }

    private fun copyToClipboardAndToast(message: String?): Boolean {
        copyToClipboard(message ?: VALUE_EMPTY)
        toast(getString(R.string.message_copy_successful))
        return true
    }
}