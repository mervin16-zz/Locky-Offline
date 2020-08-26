package com.th3pl4gu3.locky_offline.ui.main.main.account

import android.os.Bundle
import android.os.SystemClock
import android.view.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialSharedAxis
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.credentials.Account
import com.th3pl4gu3.locky_offline.core.credentials.Credentials
import com.th3pl4gu3.locky_offline.core.tuning.AccountSort
import com.th3pl4gu3.locky_offline.databinding.FragmentAccountBinding
import com.th3pl4gu3.locky_offline.ui.main.main.CredentialListener
import com.th3pl4gu3.locky_offline.ui.main.main.CredentialsPagingAdapter
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.*
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_ACCOUNTS_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_CREDENTIAL_RESTORE
import kotlinx.coroutines.launch

class AccountFragment : Fragment(), CredentialListener {

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

        /* Transition animation on startup */
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

        /* Observe the account event changes */
        subscribeAccounts()

        /* Observe back stack entry for sort changes */
        observeBackStackEntryForSortSheet()
    }

    override fun onCredentialClicked(credential: Credentials) {
        /* The click listener to handle account on clicks */
        navigateTo(
            AccountFragmentDirections.actionFragmentAccountToFragmentViewAccount(
                credential as Account
            )
        )
    }

    override fun onViewClicked(view: View, credential: Credentials) {
        /*
        * We disable view to prevent
        * rapid double clicking
        */
        view.apply {
            isEnabled = false
        }

        /*
        * The click listener to handle password viewing for each accounts
        * We also pass a reference of the view to enable it
        * after the user dismisses the dialog
        */
        showPasswordDialog(view, (credential as Account).password)
    }

    override fun onCredentialLongPressed(credential: Credentials): Boolean {
        /* Triggers upon long pressing an account */
        navigateTo(
            AccountFragmentDirections.actionGlobalFragmentBottomDialogMoreOptions()
                .setVALUEACCOUNT(credential as Account)
        )
        return true
    }

    /*
    * Explicit private functions
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
            if (event == Lifecycle.Event.ON_RESUME) {

                if (navBackStackEntry.savedStateHandle.contains(KEY_CREDENTIAL_RESTORE)) {
                    with(
                        navBackStackEntry.savedStateHandle.get<Account>(
                            KEY_CREDENTIAL_RESTORE
                        )!!
                    ) {
                        requireMainActivity().findViewById<CoordinatorLayout>(R.id.Layout_Coordinator_Main)
                            .snack(
                                getString(
                                    R.string.message_credentials_deleted,
                                    this.entryName
                                )
                            ) {
                                action(getString(R.string.button_action_undo)) {
                                    viewModel.add(this@with)
                                }
                            }

                        navBackStackEntry.savedStateHandle.remove<Account>(KEY_CREDENTIAL_RESTORE)
                    }

                }

                if (navBackStackEntry.savedStateHandle.contains(KEY_ACCOUNTS_SORT)) {
                    viewModel.sortChange(
                        navBackStackEntry.savedStateHandle.get<AccountSort>(
                            KEY_ACCOUNTS_SORT
                        )!!
                    )

                    navBackStackEntry.savedStateHandle.remove<AccountSort>(KEY_ACCOUNTS_SORT)
                }
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

    private fun subscribeAccounts() {
        val adapter = CredentialsPagingAdapter(
            this,
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

        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                /*
                 * If accounts is not null
                 * Load recyclerview and
                 * Update the ui
                 */
                lifecycleScope.launch {
                    adapter.submitList(it as PagedList<Credentials>)
                }

                updateUI(it.size)
            }
        })
    }

    private fun updateUI(listSize: Int) {
        /*
        * Hide the loading animation
        * Alternate visibility between
        * Recyclerview & Empty View
        */
        viewModel.doneLoading(listSize)
    }

    private fun navigateToSort() {
        if (SystemClock.elapsedRealtime() - _lastClickTime >= 800) {
            _lastClickTime = SystemClock.elapsedRealtime()
            navigateTo(AccountFragmentDirections.actionFragmentAccountToFragmentBottomDialogFilterAccount())
        }
    }

    private fun showPasswordDialog(clickedView: View, password: String): Boolean {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                getString(
                    R.string.text_title_alert_showPassword
                )
            )
            .setMessage(
                password.setColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorAccent
                    )
                )
            )
            .setPositiveButton(R.string.button_action_close) { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                /*
                * We enable the view to allow further clicking
                */
                clickedView.apply {
                    isEnabled = true
                }
            }
            .show()
        return true
    }
}
