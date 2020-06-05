package com.th3pl4gu3.locky_offline.ui.main.main.about.donate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewDonationBinding


class DonationItemAdapter(
    private val itemClickListener: DonationClickListener
) : ListAdapter<SkuDetails, DonationItemAdapter.ViewHolder>(
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
            skuDetails: SkuDetails
        ) {
            binding.skuDetails = skuDetails
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

class DonationDiffCallback : DiffUtil.ItemCallback<SkuDetails>() {

    override fun areItemsTheSame(oldItem: SkuDetails, newItem: SkuDetails): Boolean {
        return oldItem.sku == newItem.sku
    }

    override fun areContentsTheSame(oldItem: SkuDetails, newItem: SkuDetails): Boolean {
        return oldItem == newItem
    }

}

class DonationClickListener(val clickListener: (skuDetails: SkuDetails) -> Unit) {
    fun onClick(skuDetails: SkuDetails) = clickListener(skuDetails)
}
