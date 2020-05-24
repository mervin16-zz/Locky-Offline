package com.th3pl4gu3.locky.ui.main.main.about.donate

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky.databinding.CustomViewRecyclerviewDonationBinding


class DonationItemAdapter(
    private val itemClickListener: DonationClickListener
) : ListAdapter<Donation, DonationItemAdapter.ViewHolder>(
    DonationDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemClickListener, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewDonationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: DonationClickListener,
            donation: Donation
        ) {
            binding.donation = donation
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewDonationBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class DonationDiffCallback : DiffUtil.ItemCallback<Donation>() {

    override fun areItemsTheSame(oldItem: Donation, newItem: Donation): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Donation, newItem: Donation): Boolean {
        return oldItem == newItem
    }

}

class DonationClickListener(val clickListener: (donation: Donation) -> Unit) {
    fun onClick(donation: Donation) = clickListener(donation)
}

data class Donation(
    var title: String,
    var price: String,
    var icon: Drawable
)
