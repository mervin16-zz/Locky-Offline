package com.th3pl4gu3.locky_offline.ui.main.main.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.core.main.Card
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewCardBinding

class CardAdapter(
    private val clickListener: ClickListener,
    private val optionsClickListener: OptionsClickListener?,
    private var isSimplified: Boolean
) : ListAdapter<Card, CardAdapter.ViewHolder>(
    DiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clickListener, optionsClickListener, getItem(position), isSimplified)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: ClickListener,
            optionsClickListener: OptionsClickListener?,
            card: Card,
            isSimplified: Boolean
        ) {
            binding.card = card
            binding.clickListener = clickListener
            binding.clickOptionsListener = optionsClickListener
            binding.isSimplifiedVersion = isSimplified
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomViewRecyclerviewCardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Card>() {

    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }

}

class ClickListener(val clickListener: (card: Card) -> Unit) {
    fun onClick(card: Card) = clickListener(card)
}

class OptionsClickListener(val clickListener: (view: View, card: Card) -> Unit) {
    fun onClick(view: View, card: Card) = clickListener(view, card)
}
