package com.th3pl4gu3.locky_offline.ui.main.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.core.main.credentials.Credentials
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewCredentialsBinding

class CredentialsAdapter(
    private val clickListener: ClickListener,
    private val optionsClickListener: OptionsClickListener?,
    private val isSimplified: Boolean
) : ListAdapter<Credentials, CredentialsAdapter.ViewHolder>(
    DiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            clickListener,
            optionsClickListener,
            getItem(position),
            isSimplified
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewCredentialsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: ClickListener,
            optionsClickListener: OptionsClickListener?,
            credential: Credentials,
            isSimplified: Boolean
        ) {
            binding.credential = credential
            /*binding.credential = BasicCredential*/
            binding.clickListener = clickListener
            binding.optionsClickListener = optionsClickListener
            binding.isSimplifiedVersion = isSimplified
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewCredentialsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Credentials>() {

    override fun areItemsTheSame(oldItem: Credentials, newItem: Credentials): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Credentials, newItem: Credentials): Boolean {
        return oldItem.equals(newItem)
    }

}

class ClickListener(val clickListener: (credential: Credentials) -> Unit) {
    fun onClick(credential: Credentials) = clickListener(credential)
}

class OptionsClickListener(val clickListener: (view: View, credential: Credentials) -> Unit) {
    fun onClick(view: View, credential: Credentials) = clickListener(view, credential)
}