package com.th3pl4gu3.locky.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky.core.Card
import com.th3pl4gu3.locky.databinding.CustomViewRecyclerviewCardBinding

class CardAdapter(val clickListener: CardClickListener) : ListAdapter<Card, CardAdapter.ViewHolder>(CardDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: CardClickListener, card: Card) {
            binding.card = card
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomViewRecyclerviewCardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CardDiffCallback: DiffUtil.ItemCallback<Card>() {

    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }

}

class CardClickListener(val clickListener: (card: Card) -> Unit){
    fun onClick(card: Card) = clickListener(card)
}