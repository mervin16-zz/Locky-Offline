package com.th3pl4gu3.locky.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky.core.Account
import com.th3pl4gu3.locky.databinding.CustomViewRecyclerviewAccountBinding

class AccountAdapter(
    private val accountClickListener: AccountClickListener,
    private val accountOptionsClickListener: AccountOptionsClickListener
    ) : ListAdapter<Account, AccountAdapter.ViewHolder>(AccountDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(accountClickListener, accountOptionsClickListener, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewAccountBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: AccountClickListener, optionsClickListener: AccountOptionsClickListener, Account: Account) {
            binding.account = Account
            binding.clickListener = clickListener
            binding.optionsClickListener = optionsClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomViewRecyclerviewAccountBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class AccountDiffCallback: DiffUtil.ItemCallback<Account>() {

    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }

}

class AccountClickListener(val clickListener: (account: Account) -> Unit){
    fun onClick(account:  Account) = clickListener(account)
}

class AccountOptionsClickListener(val clickListener: (view: View, account: Account) -> Unit){
    fun onClick(view: View, account:  Account) = clickListener(view, account)
}