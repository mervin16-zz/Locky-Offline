package com.th3pl4gu3.locky_offline.ui.main.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewCredentialsFieldBinding
import com.th3pl4gu3.locky_offline.ui.main.utils.Constants

class CredentialsViewAdapter(
    private val copyClickListener: CopyClickListener,
    private val shareClickListener: ShareClickListener? = null,
    private val linkClickListener: LinkClickListener? = null,
    private val viewClickListener: ViewClickListener? = null
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
            copyClickListener,
            shareClickListener,
            linkClickListener,
            viewClickListener,
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
            copyClickListener: CopyClickListener,
            shareClickListener: ShareClickListener?,
            linkClickListener: LinkClickListener?,
            viewClickListener: ViewClickListener?,
            credentialsField: CredentialsField
        ) {
            binding.credentialsField = credentialsField
            binding.copyClickListener = copyClickListener
            binding.shareClickListener = shareClickListener
            binding.linkClickListener = linkClickListener
            binding.viewClickListener = viewClickListener
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

class CopyClickListener(val clickListener: (actualData: String) -> Unit) {
    fun onClick(actualData: String) = clickListener(actualData)
}

class ShareClickListener(val clickListener: (actualData: String) -> Unit) {
    fun onClick(actualData: String) = clickListener(actualData)
}

class LinkClickListener(val clickListener: (actualData: String) -> Unit) {
    fun onClick(actualData: String) = clickListener(actualData)
}

class ViewClickListener(val clickListener: (actualData: String) -> Unit) {
    fun onClick(actualData: String) = clickListener(actualData)
}

data class CredentialsField(
    var subtitle: String = Constants.VALUE_EMPTY,
    var data: String = Constants.VALUE_EMPTY,
    var isShareable: Boolean = false,
    var isLinkable: Boolean = false,
    var isCopyable: Boolean = false,
    var isViewable: Boolean = false
)