package com.th3pl4gu3.locky_offline.ui.main.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewCredentialsFieldBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.static_helpers.Constants.VALUE_EMPTY

class CredentialsViewAdapter(
    private val viewCredentialListener: ViewCredentialListener
) : ListAdapter<CredentialsField, CredentialsViewAdapter.ViewHolder>(
    diffCallback
) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<CredentialsField>() {

            override fun areItemsTheSame(
                oldItem: CredentialsField,
                newItem: CredentialsField
            ): Boolean {
                return oldItem.subtitle == newItem.subtitle
            }

            override fun areContentsTheSame(
                oldItem: CredentialsField,
                newItem: CredentialsField
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            viewCredentialListener,
            getItem(position)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewCredentialsFieldBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            viewCredentialListener: ViewCredentialListener,
            credentialsField: CredentialsField
        ) {
            binding.credentialsField = credentialsField
            binding.listener = viewCredentialListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomViewRecyclerviewCredentialsFieldBinding.inflate(
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

data class CredentialsField(
    var subtitle: String = VALUE_EMPTY,
    var data: String = VALUE_EMPTY,
    var isShareable: Boolean = false,
    var isLinkable: Boolean = false,
    var isCopyable: Boolean = false,
    var isViewable: Boolean = false
)