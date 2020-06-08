package com.th3pl4gu3.locky_offline.ui.main.main.about.donate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewDonationBinding
import com.th3pl4gu3.locky_offline.repository.billing.AugmentedSkuDetails


class DonationItemAdapter(
    private val itemClickListener: DonationClickListener
) : ListAdapter<AugmentedSkuDetails, DonationItemAdapter.ViewHolder>(
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
            skuDetails: AugmentedSkuDetails
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

class DonationDiffCallback : DiffUtil.ItemCallback<AugmentedSkuDetails>() {

    override fun areItemsTheSame(
        oldItem: AugmentedSkuDetails,
        newItem: AugmentedSkuDetails
    ): Boolean {
        return oldItem.sku == newItem.sku
    }

    override fun areContentsTheSame(
        oldItem: AugmentedSkuDetails,
        newItem: AugmentedSkuDetails
    ): Boolean {
        return oldItem == newItem
    }

}

class DonationClickListener(val clickListener: (skuDetails: AugmentedSkuDetails) -> Unit) {
    fun onClick(skuDetails: AugmentedSkuDetails) = clickListener(skuDetails)
}
