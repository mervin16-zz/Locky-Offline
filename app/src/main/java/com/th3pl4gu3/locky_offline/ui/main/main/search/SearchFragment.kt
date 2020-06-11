package com.th3pl4gu3.locky_offline.ui.main.main.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.databinding.FragmentSearchBinding
import com.th3pl4gu3.locky_offline.ui.main.main.account.AccountAdapter
import com.th3pl4gu3.locky_offline.ui.main.main.account.AccountClickListener
import com.th3pl4gu3.locky_offline.ui.main.main.account.AccountOptionsClickListener
import com.th3pl4gu3.locky_offline.ui.main.main.card.CardAdapter
import com.th3pl4gu3.locky_offline.ui.main.main.card.CardClickListener
import com.th3pl4gu3.locky_offline.ui.main.main.card.CardOptionsClickListener
import com.th3pl4gu3.locky_offline.ui.main.utils.*


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private var _viewModel: SearchViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        /* Attach view model to layout */
        binding.viewModel = viewModel
        // Bind lifecycle owner
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAccountsEvent()

        observeCardListEvent()

        observeSnackBarEvent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_searchview, menu)

        val searchView = menu.findItem(R.id.Menu_Search).actionView as SearchView

        (searchView).apply {

            isIconified = false
            isFocusable = true

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.search(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    searchView.clearFocus()
                    return true
                }
            })

            setOnCloseListener {
                viewModel.cancel()
                true
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeSnackBarEvent() {
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                snackBarAction(it)
            }
        })
    }

    private fun observeAccountsEvent() {
        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                initiateAccountList().submitList(it)
            }
        })
    }

    private fun observeCardListEvent() {
        viewModel.cards.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                initiateCardList().submitList(it)
            }
        })
    }

    private fun initiateAccountList(): AccountAdapter {
        val accountAdapter =
            AccountAdapter(
                AccountClickListener {
                    navigateToSelectedAccount(it)
                },
                AccountOptionsClickListener { view, account ->
                    createActionPopupMenu(view, account)
                })

        binding.RecyclerViewAccounts.apply {
            adapter = accountAdapter
            setHasFixedSize(true)
        }

        return accountAdapter
    }

    private fun initiateCardList(): CardAdapter {
        val cardAdapter = CardAdapter(
            CardClickListener {
                navigateToSelectedCard(it)
            },
            CardOptionsClickListener { view, card ->
                createCardPopupMenu(view, card)
            })

        binding.RecyclerViewCards.apply {
            adapter = cardAdapter
            setHasFixedSize(true)
        }

        return cardAdapter
    }

    private fun snackBarAction(message: String) {
        binding.LayoutParent.snackbar(message) {
            action(getString(R.string.button_snack_action_close)) { dismiss() }
        }
        viewModel.doneShowingSnackBar()
    }

    private fun triggerSnackBarAction(message: String): Boolean {
        viewModel.setSnackBarMessage(message)
        return true
    }

    private fun navigateToSelectedAccount(account: Account) {
        navigateTo(
            SearchFragmentDirections.actionFragmentSearchToFragmentViewAccount(
                account
            )
        )
    }

    private fun navigateToSelectedCard(card: Card) {
        navigateTo(
            SearchFragmentDirections.actionFragmentSearchToFragmentViewCard(
                card
            )
        )
    }

    private fun createCardPopupMenu(view: View, card: Card) {
        createPopUpMenu(
            view,
            R.menu.menu_moreoptions_card,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyNumber -> copyToClipboardAndToast(card.number.toCreditCardFormat())
                    R.id.Menu_CopyPin -> copyToClipboardAndToast(card.pin)
                    R.id.Menu_ShowPin -> triggerSnackBarAction(card.pin)
                    else -> false
                }
            }, PopupMenu.OnDismissListener {
                view.apply {
                    isEnabled = true
                }
            })
    }

    private fun createActionPopupMenu(view: View, account: Account) {
        createPopUpMenu(
            view,
            R.menu.menu_moreoptions_account,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyUsername -> copyToClipboardAndToast(account.username)
                    R.id.Menu_CopyPass -> copyToClipboardAndToast(account.password)
                    R.id.Menu_ShowPass -> triggerSnackBarAction(account.password)
                    else -> false
                }
            }, PopupMenu.OnDismissListener {
                view.apply {
                    isEnabled = true
                }
            })
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }
}
