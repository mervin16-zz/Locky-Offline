package com.th3pl4gu3.locky.ui.main.main.account

import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.databinding.FragmentAccountBinding
import com.th3pl4gu3.locky.ui.main.utils.*


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

        //Observe data when to show snack bar for "Show Password"
        _viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.LayoutFragmentAccount.snackbar(it) {
                    action(getString(R.string.button_snack_action_close)) { dismiss() }
                }

                _viewModel.doneShowingSnackbar()
            }
        })

        initiateAccountList().submitList(_viewModel.generateDummyAccounts())

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
