package com.th3pl4gu3.locky.ui.main.main.card

import android.os.Bundle
import android.os.SystemClock
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.main.Card
import com.th3pl4gu3.locky.core.main.CardSort
import com.th3pl4gu3.locky.databinding.FragmentCardBinding
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_CARDS_SORT

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
        // Inflate the layout for this fragment
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        // Fetch view model
        _viewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        //Bind view model to layout
        binding.viewModel = _viewModel
        // Bind lifecycle owner
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Observe snack bar event for any trigger
        observeSnackBarEvent()

        //Observe cards list being updated
        observeCardsEvent()

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

    private fun observeSnackBarEvent() {
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                snackBarAction(it)
            }
        })
    }

    private fun observeCardsEvent() {
        with(viewModel) {
            cards.observe(viewLifecycleOwner, Observer { cards ->
                if (cards != null) {
                    //set loading flag to hide progress bar
                    doneLoading()

                    //Alternate visibility for account list and empty view
                    alternateCardListVisibility(cards.size)

                    //Submit the cards
                    initiateCardList().submitList(cards)
                }
            })
        }
    }

    private fun observeBackStackEntryForFilterSheet() {
        val navBackStackEntry = findNavController().currentBackStackEntry!!
        navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (
                event == Lifecycle.Event.ON_RESUME &&
                navBackStackEntry.savedStateHandle.contains(KEY_CARDS_SORT)
            ) {
                val sort = navBackStackEntry.savedStateHandle.get<CardSort>(KEY_CARDS_SORT)!!

                if (sort.hasChanges()) {
                    viewModel.refreshSort(sort)
                }

                navBackStackEntry.savedStateHandle.remove<String>(KEY_CARDS_SORT)
            }
        })
    }

    private fun initiateCardList(): CardAdapter {
        val cardAdapter = CardAdapter(
            CardClickListener {
                navigateToSelectedCard(it)
            },
            CardOptionsClickListener { view, card ->
                //Prevents double click and creating a double instance
                view.apply {
                    isEnabled = false
                }
                createPopupMenu(view, card)
            })

        binding.RecyclerViewCard.apply {
            adapter = cardAdapter
            setHasFixedSize(true)
        }

        return cardAdapter
    }

    private fun navigateToFilterSheet() {
        if (SystemClock.elapsedRealtime() - _lastClickTime >= 800) {
            _lastClickTime = SystemClock.elapsedRealtime()
            findNavController().navigate(CardFragmentDirections.actionFragmentCardToBottomSheetFragmentCardFilter())
        }
    }

    private fun navigateToSelectedCard(card: Card) {
        findNavController().navigate(
            CardFragmentDirections.actionFragmentCardToFragmentViewCard(
                card
            )
        )
    }

    private fun createPopupMenu(view: View, card: Card) {
        requireContext().createPopUpMenu(
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

    private fun snackBarAction(message: String) {
        binding.LayoutFragmentCard.snackbar(message) {
            action(getString(R.string.button_snack_action_close)) { dismiss() }
        }
        viewModel.doneShowingSnackBar()
    }

    private fun triggerSnackBarAction(message: String): Boolean {
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
