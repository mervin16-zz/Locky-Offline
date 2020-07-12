package com.th3pl4gu3.locky_offline.ui.main.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewLogoAccentBinding

class LogoAccentAdapter(
    private val logoAccentClickListener: LogoAccentClickListener
) : ListAdapter<String, LogoAccentAdapter.ViewHolder>(
    LogoDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            logoAccentClickListener,
            getItem(position)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewLogoAccentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: LogoAccentClickListener,
            hex: String
        ) {
            binding.hex = hex
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewLogoAccentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class LogoDiffCallback: DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}

class LogoAccentClickListener(val clickListener: (hex: String) -> Unit) {
    fun onClick(hex: String) = clickListener(hex)
}