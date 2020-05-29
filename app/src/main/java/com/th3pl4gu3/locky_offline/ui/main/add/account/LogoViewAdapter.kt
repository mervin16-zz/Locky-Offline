package com.th3pl4gu3.locky_offline.ui.main.add.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.repository.network.WebsiteLogo
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewLogoBinding

class LogoViewAdapter(
    private val clickListener: ClickListener
) : ListAdapter<WebsiteLogo, LogoViewAdapter.ViewHolder>(
    CardDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewLogoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: ClickListener, websiteLogo: WebsiteLogo) {
            binding.logo = websiteLogo
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewLogoBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class CardDiffCallback : DiffUtil.ItemCallback<WebsiteLogo>() {

    override fun areItemsTheSame(oldItem: WebsiteLogo, newItem: WebsiteLogo): Boolean {
        return oldItem.domain == newItem.domain
    }

    override fun areContentsTheSame(oldItem: WebsiteLogo, newItem: WebsiteLogo): Boolean {
        return oldItem == newItem
    }

}

class ClickListener(val clickListener: (data: String) -> Unit) {
    fun onClick(data: String) = clickListener(data)
}