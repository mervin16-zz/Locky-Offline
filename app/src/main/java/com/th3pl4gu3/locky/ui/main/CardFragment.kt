package com.th3pl4gu3.locky.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.databinding.FragmentCardBinding
import com.th3pl4gu3.locky.ui.main.utils.toast

class CardFragment : Fragment() {

    private var _binding: FragmentCardBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCardBinding.inflate(inflater, container, false)

        initiateCardList().submitList(generateDummyCards())

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initiateCardList(): CardAdapter{
        val cardAdapter = CardAdapter(CardClickListener { card ->
            context!!.toast("Card number ${card.number} was clicked. ID: ${card.id}")
        })

        _binding!!.RecyclerViewCard.adapter = cardAdapter

        return cardAdapter
    }

    private fun generateDummyCards(): ArrayList<Card>{
        val card1 = Card("1")
        card1.number = 4850508089006089

        val card2 = Card("2")
        card2.number = 5150508089006089

        val card3 = Card("3")
        card3.number = 5450508089006089

        val card4 = Card("4")
        card4.number = 3550508089006089

        val card5 = Card("5")
        card5.number = 3000508089006089

        val card6 = Card("6")
        card6.number = 6011508089006089

        val card7 = Card("7")
        card7.number = 2130508089006089

        val card8 = Card("8")
        card8.number = 1800508089006089

        val cards = java.util.ArrayList<Card>()
        cards.add(card1)
        cards.add(card2)
        cards.add(card3)
        cards.add(card4)
        cards.add(card5)
        cards.add(card6)
        cards.add(card7)
        cards.add(card8)

        return cards
    }
}
