package com.th3pl4gu3.locky_offline.ui.main.main.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.BankAccount
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.databinding.FragmentSearchBinding
import com.th3pl4gu3.locky_offline.ui.main.main.account.AccountAdapter
import com.th3pl4gu3.locky_offline.ui.main.main.account.AccountClickListener
import com.th3pl4gu3.locky_offline.ui.main.main.bank_account.BankAccountAdapter
import com.th3pl4gu3.locky_offline.ui.main.main.bank_account.BankAccountClickListener
import com.th3pl4gu3.locky_offline.ui.main.main.card.CardAdapter
import com.th3pl4gu3.locky_offline.ui.main.main.card.CardClickListener
import com.th3pl4gu3.locky_offline.ui.main.utils.createPopUpMenu
import com.th3pl4gu3.locky_offline.ui.main.utils.navigateTo

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

        binding.ButtonFilter.setOnClickListener {
            it.apply {
                isEnabled = false
            }
            createFilterPopUpMenu(it)
        }

        observeAccounts()

        observeCards()

        observeBankAccounts()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
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

        searchView.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeAccounts() {
        viewModel.accounts.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.updateResultSize(it.size)
                subscribeAccountsUi(it)
            }
        })
    }

    private fun observeCards() {
        viewModel.cards.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.updateResultSize(it.size)
                subscribeCardsUi(it)
            }
        })
    }

    private fun observeBankAccounts() {
        viewModel.bankAccounts.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.updateResultSize(it.size)
                subscribeBankAccountsUi(it)
            }
        })
    }

    private fun createFilterPopUpMenu(view: View) {
        createPopUpMenu(
            view,
            R.menu.menu_search_filters,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_Account -> {
                        viewModel.setFilter(SearchViewModel.CREDENTIALS.ACCOUNTS)
                        true
                    }
                    R.id.Menu_Card -> {
                        viewModel.setFilter(SearchViewModel.CREDENTIALS.CARDS)
                        true
                    }
                    R.id.Menu_Bank_Account -> {
                        viewModel.setFilter(SearchViewModel.CREDENTIALS.BANK_ACCOUNTS)
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

    private fun subscribeAccountsUi(accounts: List<Account>) {
        val adapter = AccountAdapter(
            /* The click listener to handle account on clicks */
            AccountClickListener {
                navigateTo(SearchFragmentDirections.actionFragmentSearchToFragmentViewAccount(it))
            },
            null,
            true
        )

        binding.RecyclerViewLists.apply {
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

    private fun subscribeCardsUi(cards: List<Card>) {
        val adapter = CardAdapter(
            CardClickListener {
                navigateTo(SearchFragmentDirections.actionFragmentSearchToFragmentViewCard(it))
            },
            null,
            true
        )

        binding.RecyclerViewLists.apply {
            /*
            * State that layout size will not change for better performance
            */
            setHasFixedSize(true)

            /* Bind the layout manager */
            layoutManager = LinearLayoutManager(requireContext())

            /* Bind the adapter */
            this.adapter = adapter
        }

        adapter.submitList(cards)
    }

    private fun subscribeBankAccountsUi(bankAccounts: List<BankAccount>) {
        val adapter = BankAccountAdapter(
            BankAccountClickListener {
                navigateTo(SearchFragmentDirections.actionFragmentSearchToFragmentViewBankAccount(it))
            },
            null,
            true
        )

        binding.RecyclerViewLists.apply {
            /*
            * State that layout size will not change for better performance
            */
            setHasFixedSize(true)

            /* Bind the layout manager */
            layoutManager = LinearLayoutManager(requireContext())

            /* Bind the adapter */
            this.adapter = adapter
        }

        adapter.submitList(bankAccounts)
    }
}
