package com.th3pl4gu3.locky_offline.ui.main.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.core.others.Statistic
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewStatisticsBinding


class StatisticsAdapter : ListAdapter<Statistic, StatisticsAdapter.ViewHolder>(
    DiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            statistic: Statistic
        ) {
            binding.statistic = statistic
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewStatisticsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Statistic>() {

    override fun areItemsTheSame(
        oldItem: Statistic,
        newItem: Statistic
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: Statistic,
        newItem: Statistic
    ): Boolean {
        return oldItem == newItem
    }
}
