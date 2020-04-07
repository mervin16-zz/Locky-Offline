package com.th3pl4gu3.locky.ui.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky.databinding.CustomViewRecyclerviewCredentialsBinding
import com.th3pl4gu3.locky.ui.main.utils.Constants

class CredentialsViewAdapter(
    private val copyClickListener: CopyClickListener,
    private val viewClickListener: ViewClickListener
) : ListAdapter<CredentialsField, CredentialsViewAdapter.ViewHolder>(
    CardDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(copyClickListener, viewClickListener, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewCredentialsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(copyClickListener: CopyClickListener, viewClickListener: ViewClickListener, credentialsField: CredentialsField) {
            binding.credentialsField = credentialsField
            binding.copyClickListener = copyClickListener
            binding.viewClickListener = viewClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomViewRecyclerviewCredentialsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class CardDiffCallback: DiffUtil.ItemCallback<CredentialsField>() {

    override fun areItemsTheSame(oldItem: CredentialsField, newItem: CredentialsField): Boolean {
        return oldItem.subtitle == newItem.subtitle
    }


    override fun areContentsTheSame(oldItem: CredentialsField, newItem: CredentialsField): Boolean {
        return oldItem == newItem
    }

}

class CopyClickListener(val clickListener: (data: String) -> Unit){
    fun onClick(data: String) = clickListener(data)
}

class ViewClickListener(val clickListener: (data: String) -> Unit){
    fun onClick(data: String) = clickListener(data)
}

data class CredentialsField(
    var subtitle: String = Constants.VALUE_EMPTY,
    var data: String = Constants.VALUE_EMPTY,
    var isCopyable: Int = View.GONE,
    var isViewable: Int = View.GONE
)