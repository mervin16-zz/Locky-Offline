package com.th3pl4gu3.locky_offline.ui.main.main.about

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewAboutContributorsBinding


class AboutContributorsAdapter(
    private val contributorClickListener: ContributorClickListener
) : ListAdapter<Contributor, AboutContributorsAdapter.ViewHolder>(
    ContributorDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contributorClickListener, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewAboutContributorsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: ContributorClickListener,
            contributor: Contributor
        ) {
            binding.contributor = contributor
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewAboutContributorsBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class ContributorDiffCallback : DiffUtil.ItemCallback<Contributor>() {

    override fun areItemsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
        return oldItem == newItem
    }

}

class ContributorClickListener(val clickListener: (url: String) -> Unit) {
    fun onClick(url: String) = clickListener(url)
}

data class Contributor(
    var name: String,
    var description: String,
    var icon: Drawable,
    var url: String
)
