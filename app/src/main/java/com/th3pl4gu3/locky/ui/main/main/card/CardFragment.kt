package com.th3pl4gu3.locky.ui.main.main.card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.databinding.FragmentCardBinding
import com.th3pl4gu3.locky.ui.main.utils.*

class CardFragment : Fragment() {

    private var _binding: FragmentCardBinding? = null
    private lateinit var _viewModel: CardViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(CardViewModel::class.java)
        binding.lifecycleOwner = this

        _viewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.LayoutFragmentCard.snackbar(it) {
                    action(getString(R.string.button_snack_action_close)) { dismiss() }
                }

                _viewModel.doneShowingSnackbar()
            }
        })

        initiateCardList().submitList(_viewModel.generateDummyCards())

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initiateCardList(): CardAdapter {
        val cardAdapter = CardAdapter(
            CardClickListener {
                requireView().findNavController()
                    .navigate(CardFragmentDirections.actionFragmentCardToViewCardFragment(it))
            },
            CardOptionsClickListener { view, card ->
                //displaying the popup
                createPopupMenu(view, card)
            })

        binding.RecyclerViewCard.adapter = cardAdapter

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
            })
    }

    private fun copyToClipboardAndToast(message: String): Boolean {
        requireContext().copyToClipboard(message)
        requireContext().toast(getString(R.string.message_copy_successful))
        return true
    }
}
