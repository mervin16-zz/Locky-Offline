package com.th3pl4gu3.locky.ui.main.main.card

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.th3pl4gu3.locky.R
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.databinding.FragmentCardBinding
import com.th3pl4gu3.locky.ui.main.utils.*
import com.th3pl4gu3.locky.ui.main.utils.Constants.Companion.VALUE_PARCELS_CARD
import com.th3pl4gu3.locky.ui.main.view.ViewCardActivity

class CardFragment : Fragment() {

    private var _binding: FragmentCardBinding? = null
    private lateinit var _viewModel: CardViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(CardViewModel::class.java)

        binding.viewModel = _viewModel
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
                startActivity(Intent(context, ViewCardActivity::class.java).apply {
                    putExtra(VALUE_PARCELS_CARD, it)
                })
            },
            CardOptionsClickListener { view, card ->
                //displaying the popup
                createPopupMenu(view, card)
            })

        _binding!!.RecyclerViewCard.adapter = cardAdapter

        return cardAdapter
    }

    private fun createPopupMenu(view: View, card: Card) {
        context?.createPopUpMenu(
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
        context?.copyToClipboard(message)
        context?.toast(getString(R.string.message_copy_successful))
        return true
    }
}
