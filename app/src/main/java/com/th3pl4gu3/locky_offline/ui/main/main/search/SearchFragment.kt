package com.th3pl4gu3.locky_offline.ui.main.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Account
import com.th3pl4gu3.locky_offline.core.main.BankAccount
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.core.main.Device
import com.th3pl4gu3.locky_offline.databinding.FragmentSearchBinding
import com.th3pl4gu3.locky_offline.ui.main.main.account.AccountAdapter
import com.th3pl4gu3.locky_offline.ui.main.main.bank_account.BankAccountAdapter
import com.th3pl4gu3.locky_offline.ui.main.main.card.CardAdapter
import com.th3pl4gu3.locky_offline.ui.main.main.device.DeviceAdapter
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.createPopUpMenu
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.requireMainActivity
import com.th3pl4gu3.locky_offline.ui.main.main.account.ClickListener as AccountClickListener
import com.th3pl4gu3.locky_offline.ui.main.main.bank_account.ClickListener as BankClickListener
import com.th3pl4gu3.locky_offline.ui.main.main.card.ClickListener as CardClickListener
import com.th3pl4gu3.locky_offline.ui.main.main.device.ClickListener as DeviceClickListener

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private var _viewModel: SearchViewModel? = null

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    /*
    * Extension properties to fetch views from main activity
    * Views consists of search functionality
    */
    private val toolbarTitle: TextView
        get() = requireMainActivity().findViewById(R.id.Toolbar_Main_Title)

    private val searchBox: EditText
        get() = requireMainActivity().findViewById(R.id.Toolbar_Search_Box)

    private val searchClose: ImageButton
        get() = requireMainActivity().findViewById(R.id.Toolbar_Search_Close)

    private val searchLayout: LinearLayout
        get() = requireMainActivity().findViewById(R.id.Toolbar_Search_Layout)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBox.addTextChangedListener {
            viewModel.search(it.toString())
        }

        searchClose.setOnClickListener {
            searchBox.setText("")
            viewModel.cancel()
        }

        binding.ButtonFilter.setOnClickListener {
            it.apply {
                isEnabled = false
            }
            createFilterPopUpMenu(it)
        }

        observeAccounts()

        observeCards()

        observeBankAccounts()

        observeDevices()
    }

    override fun onResume() {
        super.onResume()
        /*
        * Show text box and hide title
        */
        alternateSearchVisibility(true)
    }

    override fun onPause() {
        super.onPause()
        //Clear text box
        searchBox.text.clear()
        /*
        * Hide text box and show title
        */
        alternateSearchVisibility(false)
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

    private fun observeDevices() {
        viewModel.devices.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewModel.updateResultSize(it.size)
                subscribeDevicesUi(it)
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
                    R.id.Menu_Device -> {
                        viewModel.setFilter(SearchViewModel.CREDENTIALS.DEVICES)
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
                navigateTo(
                    SearchFragmentDirections.actionFragmentSearchToFragmentViewAccount(
                        it
                    )
                )
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
            BankClickListener {
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

    private fun subscribeDevicesUi(devices: List<Device>) {
        val adapter = DeviceAdapter(
            DeviceClickListener {
                navigateTo(SearchFragmentDirections.actionFragmentSearchToFragmentViewDevice(it))
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

        adapter.submitList(devices)
    }

    private fun alternateSearchVisibility(visible: Boolean) {
        if (visible) {
            toolbarTitle.visibility =
                View.GONE
            searchLayout.visibility =
                View.VISIBLE
        } else {
            toolbarTitle.visibility =
                View.VISIBLE
            searchLayout.visibility =
                View.GONE
        }
    }

}
