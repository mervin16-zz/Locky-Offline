package com.th3pl4gu3.locky_offline.ui.main.main.card

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
import com.th3pl4gu3.locky_offline.core.credentials.Card
import com.th3pl4gu3.locky_offline.core.credentials.Credentials
import com.th3pl4gu3.locky_offline.core.tuning.CardSort
import com.th3pl4gu3.locky_offline.databinding.FragmentCardBinding
import com.th3pl4gu3.locky_offline.ui.main.main.CredentialListener
import com.th3pl4gu3.locky_offline.ui.main.main.CredentialsPagingAdapter
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.*
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_CARDS_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.KEY_CREDENTIAL_RESTORE
import kotlinx.coroutines.launch

class CardFragment : Fragment(), CredentialListener {

    private var _binding: FragmentCardBinding? = null
    private var _viewModel: CardViewModel? = null
    private var _lastClickTime: Long = 0

    private val binding get() = _binding!!
    private val viewModel get() = _viewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*Inflate the layout for this fragment*/
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        /*Fetch view model*/
        _viewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        /*Bind view model to layout*/
        binding.viewModel = _viewModel
        /*Bind lifecycle owner*/
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Hides the soft keyboard */
        hideSoftKeyboard(binding.root)

        /*Observe cards list being updated*/
        subscribeUi()

        /*Observe sort & filter changes*/
        observeBackStackEntryForSortSheet()
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
                navigateToSortSheet()
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCredentialClicked(credential: Credentials) {
        /* The click listener to handle card on clicks */
        navigateTo(
            CardFragmentDirections.actionFragmentCardToFragmentViewCard(
                credential as Card
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
        * The click listener to handle password viewing for each cards
        * We also pass a reference of the view to enable it
        * after the user dismisses the dialog
        */
        showPasswordDialog(view, (credential as Card).pin)
    }

    override fun onCredentialLongPressed(credential: Credentials): Boolean {
        /* Triggers upon long pressing a card */
        navigateTo(
            CardFragmentDirections.actionGlobalFragmentBottomDialogMoreOptions()
                .setVALUECARD(credential as Card)
        )
        return true
    }

    private fun updateUI(listSize: Int) {
        /*
        * Hide the loading animation
        * Alternate visibility between
        * Recyclerview & Empty View
        */
        viewModel.doneLoading(listSize)
    }

    private fun observeBackStackEntryForSortSheet() {
        val navController = findNavController()
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        val navBackStackEntry = navController.getBackStackEntry(R.id.Fragment_Card)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {

                if (navBackStackEntry.savedStateHandle.contains(KEY_CREDENTIAL_RESTORE)) {
                    with(
                        navBackStackEntry.savedStateHandle.get<Card>(
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

                        navBackStackEntry.savedStateHandle.remove<Card>(KEY_CREDENTIAL_RESTORE)
                    }
                }
                if (navBackStackEntry.savedStateHandle.contains(KEY_CARDS_SORT)) {
                    viewModel.sortChange(
                        navBackStackEntry.savedStateHandle.get<CardSort>(
                            KEY_CARDS_SORT
                        )!!
                    )

                    navBackStackEntry.savedStateHandle.remove<CardSort>(KEY_CARDS_SORT)
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

    private fun subscribeUi() {
        val adapter = CredentialsPagingAdapter(
            this,
            false
        )

        binding.RecyclerViewCard.apply {
            /*
            * State that layout size will not change for better performance
            */
            setHasFixedSize(true)

            /* Bind the layout manager */
            layoutManager = LinearLayoutManager(requireContext())

            /* Bind the adapter */
            this.adapter = adapter
        }

        viewModel.cards.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                lifecycleScope.launch {
                    adapter.submitList(it as PagedList<Credentials>)
                }

                //Update UI
                updateUI(it.size)
            }
        })
    }

    private fun navigateToSortSheet() {
        if (SystemClock.elapsedRealtime() - _lastClickTime >= 800) {
            _lastClickTime = SystemClock.elapsedRealtime()
            navigateTo(CardFragmentDirections.actionFragmentCardToFragmentBottomDialogFilterCard())
        }
    }

    private fun showPasswordDialog(clickedView: View, password: String): Boolean {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                getString(
                    R.string.text_title_alert_showPin
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
