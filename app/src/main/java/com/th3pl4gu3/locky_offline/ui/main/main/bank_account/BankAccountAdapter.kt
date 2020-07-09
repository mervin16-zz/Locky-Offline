package com.th3pl4gu3.locky_offline.ui.main.main.bank_account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.core.main.BankAccount
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewBankaccountBinding

class BankAccountAdapter(
    private val clickListener: ClickListener,
    private val optionsClickListener: OptionsClickListener?,
    private var isSimplified: Boolean
) : ListAdapter<BankAccount, BankAccountAdapter.ViewHolder>(
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

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewBankaccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: ClickListener,
            optionsClickListener: OptionsClickListener?,
            bankAccount: BankAccount,
            isSimplified: Boolean
        ) {
            binding.bankAccount = bankAccount
            binding.clickListener = clickListener
            binding.optionsClickListener = optionsClickListener
            binding.isSimplifiedVersion = isSimplified
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewBankaccountBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<BankAccount>() {

    override fun areItemsTheSame(oldItem: BankAccount, newItem: BankAccount): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BankAccount, newItem: BankAccount): Boolean {
        return oldItem == newItem
    }

}

class ClickListener(val clickListener: (account: BankAccount) -> Unit) {
    fun onClick(account: BankAccount) = clickListener(account)
}

class OptionsClickListener(val clickListener: (view: View, account: BankAccount) -> Unit) {
    fun onClick(view: View, account: BankAccount) = clickListener(view, account)
}