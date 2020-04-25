package com.th3pl4gu3.locky.ui.main.main.account

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.core.AccountRefine
import com.th3pl4gu3.locky.databinding.FragmentAccountBinding
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_ACCOUNTS_FILTER
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_ACCOUNTS_SORT


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private lateinit var _viewModel: AccountViewModel
    private var _lastClickTime: Long = 0
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        // Fetch view model
        _viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        // Bind lifecycle owner
        binding.lifecycleOwner = this

        //Observe data when to show snack bar for "Show Pin"
        with(_viewModel) {

            //set loading flag to show loading progress bar
            setLoading(LoadingStatus.LOADING)

            showSnackBarEvent.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    binding.LayoutFragmentAccount.snackbar(it) {
                        action(getString(R.string.button_snack_action_close)) { dismiss() }
                    }

                    doneShowingSnackBar()
                }
            })

            //Observe loading status for any trigger for the progress bar
            loadingStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadingStatus.LOADING -> {
                        progressBarVisibility(View.VISIBLE)
                    }
                    LoadingStatus.DONE, LoadingStatus.ERROR -> {
                        progressBarVisibility(View.GONE)
                    }
                    else -> {
                        progressBarVisibility(View.GONE)
                    }
                }
            })

            //Observe accounts list being updated
            accounts.observe(viewLifecycleOwner, Observer { accounts ->
                if (accounts != null) {
                    //set loading flag to hide progress bar
                    setLoading(LoadingStatus.DONE)

                    accountListVisibility(accounts)
                }
            })

            val navBackStackEntry = findNavController().currentBackStackEntry!!
            navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (
                    event == Lifecycle.Event.ON_RESUME &&
                    navBackStackEntry.savedStateHandle.contains(KEY_ACCOUNTS_FILTER) &&
                    navBackStackEntry.savedStateHandle.contains(KEY_ACCOUNTS_SORT)
                ) {
                    val filter =
                        navBackStackEntry.savedStateHandle.get<AccountRefine>(KEY_ACCOUNTS_FILTER)!!
                    val sort =
                        navBackStackEntry.savedStateHandle.get<AccountRefine>(KEY_ACCOUNTS_SORT)!!

                    //_viewModel.updateCards(filter)
                    Log.i(
                        "REFINEMENTTEST",
                        "CT:${filter.website} B:${filter.email} NC:${filter.twofa}"
                    )
                    Log.i("REFINEMENTTEST", "CT:${sort.website} B:${sort.email} NC:${sort.twofa}")

                    navBackStackEntry.savedStateHandle.remove<String>(KEY_ACCOUNTS_FILTER)
                    navBackStackEntry.savedStateHandle.remove<String>(KEY_ACCOUNTS_SORT)
                }
            })
        }

        return binding.root
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
                if (SystemClock.elapsedRealtime() - _lastClickTime >= 800) {
                    _lastClickTime = SystemClock.elapsedRealtime()
                    findNavController().navigate(AccountFragmentDirections.actionFragmentAccountToBottomSheetFragmentAccountFilter())
                }
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun progressBarVisibility(visibility: Int) {
        binding.ProgressBar.visibility = visibility
    }

    private fun accountListVisibility(accounts: List<Account>) {
        if (accounts.isEmpty()) {
            binding.EmptyView.visibility = View.VISIBLE
            binding.RecyclerViewAccount.visibility = View.GONE
        } else {
            binding.EmptyView.visibility = View.GONE
            binding.RecyclerViewAccount.visibility = View.VISIBLE
            initiateAccountList().submitList(accounts)
        }
    }

    private fun initiateAccountList(): AccountAdapter {
        val accountAdapter =
            AccountAdapter(
                AccountClickListener {
                    findNavController().navigate(
                        AccountFragmentDirections.actionFragmentAccountToFragmentViewAccount(
                            it
                        )
                    )
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

    private fun createPopupMenu(view: View, account: Account) {
        requireContext().createPopUpMenu(
            view,
            R.menu.menu_moreoptions_account,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyUsername -> copyToClipboardAndToast(account.username)
                    R.id.Menu_CopyPass -> copyToClipboardAndToast(account.password)
                    R.id.Menu_ShowPass -> {
                        _viewModel.setSnackBarMessage(account.password)
                        true
                    }
                    else -> false
                }
            }, PopupMenu.OnDismissListener {
                view.apply {
                    isEnabled = true
                }
            })
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        requireContext().copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }

    private fun toast(message: String) = requireContext().toast(message)
}
