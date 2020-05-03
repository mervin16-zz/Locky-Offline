package com.th3pl4gu3.locky.ui.main.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky.databinding.CustomViewRecyclerviewUserDetailsBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants

class UserDetailsViewAdapter : ListAdapter<UserDetails, UserDetailsViewAdapter.ViewHolder>(
    CardDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewUserDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userDetails: UserDetails) {
            binding.userDetails = userDetails
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewUserDetailsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class CardDiffCallback : DiffUtil.ItemCallback<UserDetails>() {

    override fun areItemsTheSame(oldItem: UserDetails, newItem: UserDetails): Boolean {
        return oldItem.subtitle == newItem.subtitle
    }


    override fun areContentsTheSame(oldItem: UserDetails, newItem: UserDetails): Boolean {
        return oldItem == newItem
    }

}

data class UserDetails(
    var subtitle: String = Constants.VALUE_EMPTY,
    var data: String = Constants.VALUE_EMPTY
)