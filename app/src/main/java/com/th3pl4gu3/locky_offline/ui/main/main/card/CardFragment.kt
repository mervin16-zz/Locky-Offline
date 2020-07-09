package com.th3pl4gu3.locky_offline.ui.main.main.card

import android.os.Bundle
import android.os.SystemClock
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialSharedAxis
import com.th3pl4gu3.locky_offline.R
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.core.main.CardSort
import com.th3pl4gu3.locky_offline.databinding.FragmentCardBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants.KEY_CARDS_SORT
import com.th3pl4gu3.locky_offline.ui.main.utils.extensions.*

class CardFragment : Fragment() {

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
        observeCardsEvent()

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

    private fun observeCardsEvent() {
        with(viewModel) {
            cards.observe(viewLifecycleOwner, Observer { cards ->
                if (cards != null) {
                    //Update UI
                    updateUI(cards.size)

                    //Submit the cards
                    subscribeUi(cards)
                }
            })
        }
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
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(KEY_CARDS_SORT)
            ) {

                viewModel.sortChange(navBackStackEntry.savedStateHandle.get<CardSort>(KEY_CARDS_SORT)!!)

                navBackStackEntry.savedStateHandle.remove<CardSort>(KEY_CARDS_SORT)
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

    private fun subscribeUi(cards: List<Card>) {
        val adapter = CardAdapter(
            ClickListener {
                navigateTo(CardFragmentDirections.actionFragmentCardToFragmentViewCard(it))
            },
            OptionsClickListener { view, card ->
                //Prevents double click and creating a double instance
                view.apply {
                    isEnabled = false
                }
                createPopupMenu(view, card)
            },
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

        adapter.submitList(cards)
    }

    private fun navigateToSortSheet() {
        if (SystemClock.elapsedRealtime() - _lastClickTime >= 800) {
            _lastClickTime = SystemClock.elapsedRealtime()
            navigateTo(CardFragmentDirections.actionFragmentCardToFragmentBottomDialogFilterCard())
        }
    }

    private fun createPopupMenu(view: View, card: Card) {
        createPopUpMenu(
            view,
            R.menu.menu_moreoptions_card,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyNumber -> copyToClipboardAndToast(card.number.toCreditCardFormat())
                    R.id.Menu_CopyPin -> copyToClipboardAndToast(card.pin)
                    R.id.Menu_ShowPin -> showPasswordDialog(card.pin)
                    else -> false
                }
            }, PopupMenu.OnDismissListener {
                view.apply {
                    isEnabled = true
                }
            })
    }

    private fun showPasswordDialog(password: String): Boolean {
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
            .show()
        return true
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        copyToClipboard(message)
        toast(getString(R.string.message_copy_successful))
        return true
    }
}
