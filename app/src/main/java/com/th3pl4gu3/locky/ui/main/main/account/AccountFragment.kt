package com.th3pl4gu3.locky.ui.main.main.account

import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.Account
import com.th3pl4gu3.locky.core.main.AccountSort
import com.th3pl4gu3.locky.databinding.FragmentAccountBinding
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_ACCOUNTS_SORT


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private var _viewModel: AccountViewModel? = null
    private var _lastClickTime: Long = 0

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

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

        //Observe data when to show snack bar for "Show Pin"
        observeSnackBarEvent()

        //Observe accounts list being updated
        observeAccountsEvent()

        //Observe sort & filter changes
        observeBackStackEntryForFilterSheet()
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
                navigateToFilterSheet()
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeBackStackEntryForFilterSheet() {
        val navBackStackEntry = findNavController().currentBackStackEntry!!
        navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (
                event == Lifecycle.Event.ON_RESUME &&
                navBackStackEntry.savedStateHandle.contains(KEY_ACCOUNTS_SORT)
            ) {
                val sort =
                    navBackStackEntry.savedStateHandle.get<AccountSort>(KEY_ACCOUNTS_SORT)!!

                if (sort.hasChanges()) {
                    viewModel.refresh(sort)
                }

                navBackStackEntry.savedStateHandle.remove<String>(KEY_ACCOUNTS_SORT)
            }
        })
    }

    private fun observeAccountsEvent() {
        viewModel.accounts.observe(viewLifecycleOwner, Observer { accounts ->
            if (accounts != null) {

                with(viewModel) {
                    //set loading flag to hide progress bar
                    doneLoading()

                    //Alternate visibility for account list and empty view
                    alternateAccountListVisibility(accounts.size)
                }

                //Submit list to recyclerview
                initiateAccountList().submitList(accounts)
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

    private fun navigateToFilterSheet() {
        if (SystemClock.elapsedRealtime() - _lastClickTime >= 800) {
            _lastClickTime = SystemClock.elapsedRealtime()
            findNavController().navigate(AccountFragmentDirections.actionFragmentAccountToBottomSheetFragmentAccountFilter())
        }
    }

    private fun initiateAccountList(): AccountAdapter {
        val accountAdapter =
            AccountAdapter(
                AccountClickListener {
                    navigateToSelectedAccount(it)
                },
                AccountOptionsClickListener { view, account ->
                    //Prevents double click and creating a double instance
                    view.apply {
                        isEnabled = false
                    }
                    //displaying the popup
                    createPopupMenu(view, account)
                })

        binding.RecyclerViewAccount.apply {
            adapter = accountAdapter
            setHasFixedSize(true)
        }

        return accountAdapter
    }

    private fun navigateToSelectedAccount(account: Account) {
        findNavController().navigate(
            AccountFragmentDirections.actionFragmentAccountToFragmentViewAccount(
                account
            )
        )
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

    private fun toast(message: String) = requireContext().toast(message)
}
