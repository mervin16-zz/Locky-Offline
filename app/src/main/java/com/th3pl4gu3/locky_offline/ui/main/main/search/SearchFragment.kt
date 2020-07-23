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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.credentials.*
import com.th3pl4gu3.locky_offline.databinding.FragmentSearchBinding
import com.th3pl4gu3.locky_offline.ui.main.main.ClickListener
import com.th3pl4gu3.locky_offline.ui.main.main.CredentialsAdapter
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.createPopUpMenu
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.navigateTo
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.requireMainActivity
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.toast
import kotlinx.coroutines.launch

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

        /* Observe text changes in search box*/
        searchBox.addTextChangedListener {
            viewModel.search(it.toString())
        }

        /* Observe click listener for close button */
        searchClose.setOnClickListener {
            searchBox.setText("")
            viewModel.cancel()
        }

        /* Observer click listener for filter button */
        binding.ButtonFilter.setOnClickListener {
            it.apply {
                isEnabled = false
            }
            createFilterPopUpMenu(it)
        }

        /* Observes account filter */
        observeAccounts()

        /* Observes cards filter */
        observeCards()

        /* Observes bank accounts filter */
        observeBankAccounts()

        /* Observes devices filter */
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
                /* Subscribe UI */
                subscribeUi(it)

                /* Updates the result size text */
                viewModel.updateResultSize(it.size)
            }
        })
    }

    private fun observeCards() {
        viewModel.cards.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                /* Subscribe UI */
                subscribeUi(it)

                /* Updates the result size text */
                viewModel.updateResultSize(it.size)
            }
        })
    }

    private fun observeBankAccounts() {
        viewModel.bankAccounts.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                /* Subscribe UI */
                subscribeUi(it)

                /* Updates the result size text */
                viewModel.updateResultSize(it.size)
            }
        })
    }

    private fun observeDevices() {
        viewModel.devices.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                /* Subscribe UI */
                subscribeUi(it)

                /* Updates the result size text */
                viewModel.updateResultSize(it.size)
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
                        viewModel.setFilter(CredentialIdentifier.ACCOUNTS)
                        true
                    }
                    R.id.Menu_Card -> {
                        viewModel.setFilter(CredentialIdentifier.CARDS)
                        true
                    }
                    R.id.Menu_Bank_Account -> {
                        viewModel.setFilter(CredentialIdentifier.BANK_ACCOUNTS)
                        true
                    }
                    R.id.Menu_Device -> {
                        viewModel.setFilter(CredentialIdentifier.DEVICES)
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

    private fun subscribeUi(list: List<Credentials>) {
        val adapter = CredentialsAdapter(
            /* The click listener to handle credentials on clicks */
            ClickListener {
                when (it) {
                    is Account -> navigateTo(
                        SearchFragmentDirections.actionFragmentSearchToFragmentViewAccount(
                            it
                        )
                    )
                    is Card -> navigateTo(
                        SearchFragmentDirections.actionFragmentSearchToFragmentViewCard(
                            it
                        )
                    )
                    is BankAccount -> navigateTo(
                        SearchFragmentDirections.actionFragmentSearchToFragmentViewBankAccount(
                            it
                        )
                    )
                    is Device -> navigateTo(
                        SearchFragmentDirections.actionFragmentSearchToFragmentViewDevice(
                            it
                        )
                    )
                    else -> toast(getString(R.string.error_internal_code_3))
                }
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

        /* Submit the list for displaying */
        lifecycleScope.launch {
            adapter.submitList(list)
        }
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
