package com.th3pl4gu3.locky_offline.ui.main.main.about

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewAboutSectionBinding


class AboutItemAdapter(
    private val itemClickListener: ItemClickListener
) : ListAdapter<AboutItem, AboutItemAdapter.ViewHolder>(
    AboutItemDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemClickListener, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewAboutSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: ItemClickListener,
            aboutItem: AboutItem
        ) {
            binding.aboutItem = aboutItem
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewAboutSectionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class AboutItemDiffCallback : DiffUtil.ItemCallback<AboutItem>() {

    override fun areItemsTheSame(oldItem: AboutItem, newItem: AboutItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: AboutItem, newItem: AboutItem): Boolean {
        return oldItem == newItem
    }

}

class ItemClickListener(val clickListener: (item: AboutItem.Item) -> Unit) {
    fun onClick(item: AboutItem.Item) = clickListener(item)
}

data class AboutItem(
    var title: String,
    var description: String,
    var icon: Drawable,
    var item: Item
) {
    enum class Item { DEV_RATE_US, DEV_DONATE, DEV_REPORT_BUG, DEV_SHARE, OTHER_VERSION, OTHER_DEVELOPER, OTHER_LICENSES, OTHER_POLICY }
}
