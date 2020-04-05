package com.th3pl4gu3.locky.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.databinding.FragmentAccountBinding
import com.th3pl4gu3.locky.ui.main.utils.*


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private lateinit var _viewModel: AccountViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        _viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.LayoutFragmentAccount.snackbar(it) {
                    action("Close") { dismiss() }
                }

                _viewModel.doneShowingSnackbar()
            }
        })

        initiateAccountList().submitList(_viewModel.generateDummyAccounts())

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initiateAccountList(): AccountAdapter {
        val accountAdapter = AccountAdapter(AccountClickListener { account ->
            context!!.toast("Account ${account.name} was clicked. ID: ${account.id}")
        }, AccountOptionsClickListener { view, account ->
            //displaying the popup
            createPopupMenu(view, account)
        })

        _binding!!.RecyclerViewAccount.adapter = accountAdapter

        return accountAdapter
    }

    private fun createPopupMenu(view: View, account: Account) {
        context?.createPopUpMenu(
            view,
            R.menu.menu_moreoptions_account,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyUsername -> copyToClipboardAndToast(if (account.username != null) account.username!! else account.email!!)
                    R.id.Menu_CopyPass -> copyToClipboardAndToast(account.password)
                    R.id.Menu_ShowPass -> {
                        _viewModel.setSnackBarMessage(account.password)
                        true
                    }
                    else -> false
                }
            })
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        context?.copyToClipboard(message)
        context?.toast("Copied successfully")
        return true
    }

}
