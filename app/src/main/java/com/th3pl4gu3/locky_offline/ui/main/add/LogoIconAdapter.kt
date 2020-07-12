package com.th3pl4gu3.locky_offline.ui.main.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewLogoIconBinding

class LogoIconAdapter(
    private val logoIconClickListener: LogoIconClickListener
) : ListAdapter<String, LogoIconAdapter.ViewHolder>(
    LogoIconDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            logoIconClickListener,
            getItem(position)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewLogoIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: LogoIconClickListener,
            icon: String
        ) {
            binding.icon = icon
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewLogoIconBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class LogoIconDiffCallback : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}

class LogoIconClickListener(val clickListener: (icon: String) -> Unit) {
    fun onClick(icon: String) = clickListener(icon)
}