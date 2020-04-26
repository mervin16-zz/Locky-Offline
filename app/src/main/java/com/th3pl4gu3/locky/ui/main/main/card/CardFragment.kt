package com.th3pl4gu3.locky.ui.main.main.card

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
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.core.tuning.CardSort
import com.th3pl4gu3.locky.databinding.FragmentCardBinding
import com.th3pl4gu3.locky.repository.LoadingStatus
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.KEY_CARDS_SORT

class CardFragment : Fragment() {

    private var _binding: FragmentCardBinding? = null
    private lateinit var _viewModel: CardViewModel
    private var _lastClickTime: Long = 0
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        // Fetch view model
        _viewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        // Bind lifecycle owner
        binding.lifecycleOwner = this

        //Observe data when to show snack bar for "Show Pin"
        with(_viewModel) {

            //set loading flag to show loading progress bar
            setLoading(LoadingStatus.LOADING)

            //Observe snack bar event for any trigger
            showSnackBarEvent.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    binding.LayoutFragmentCard.snackbar(it) {
                        action(getString(R.string.button_snack_action_close)) { dismiss() }
                    }
                    doneShowingSnackBar()
                }
            })

            //Observe loading status for any trigger for the progress bar
            loadingStatus.observe(viewLifecycleOwner, Observer {
                when(it){
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

            //Observe cards list being updated
            cards.observe(viewLifecycleOwner, Observer { cards ->
                if (cards != null) {
                    //set loading flag to hide progress bar
                    setLoading(LoadingStatus.DONE)

                    cardListVisibility(cards)
                }
            })

            val navBackStackEntry = findNavController().currentBackStackEntry!!
            navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (
                    event == Lifecycle.Event.ON_RESUME &&
                    navBackStackEntry.savedStateHandle.contains(KEY_CARDS_SORT)
                ) {
                    val sort = navBackStackEntry.savedStateHandle.get<CardSort>(KEY_CARDS_SORT)!!

                    if (sort.hasChanges()) {
                        //Store sort to local storage
                        LocalStorageManager.with(requireActivity().application)
                        LocalStorageManager.put(KEY_CARDS_SORT, sort)
                        _viewModel.refreshSort(sort)
                    }

                    navBackStackEntry.savedStateHandle.remove<String>(KEY_CARDS_SORT)
                }
            })
        }

        Log.i("INITTEST", "OnCreate Ended")
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
                    findNavController().navigate(CardFragmentDirections.actionFragmentCardToBottomSheetFragmentCardFilter())
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

    private fun progressBarVisibility(visibility: Int){
        binding.ProgressBar.visibility = visibility
    }

    private fun cardListVisibility(cards: List<Card>) {
        if (cards.isEmpty()) {
            binding.EmptyView.visibility = View.VISIBLE
            binding.RecyclerViewCard.visibility = View.GONE
        } else {
            binding.EmptyView.visibility = View.GONE
            binding.RecyclerViewCard.visibility = View.VISIBLE
            initiateCardList().submitList(cards)
        }
    }

    private fun initiateCardList(): CardAdapter {
        val cardAdapter = CardAdapter(
            CardClickListener {
                findNavController().navigate(
                    CardFragmentDirections.actionFragmentCardToFragmentViewCard(
                        it
                    )
                )
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

    private fun createPopupMenu(view: View, card: Card) {
        requireContext().createPopUpMenu(
            view,
            R.menu.menu_moreoptions_card,
            PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Menu_CopyNumber -> copyToClipboardAndToast(card.number.toCreditCardFormat())
                    R.id.Menu_CopyPin -> copyToClipboardAndToast(card.pin.toString())
                    R.id.Menu_ShowPin -> {
                        _viewModel.setSnackBarMessage(card.pin.toString())
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
