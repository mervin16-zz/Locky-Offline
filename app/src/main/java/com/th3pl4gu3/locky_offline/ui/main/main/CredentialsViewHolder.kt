package com.th3pl4gu3.locky_offline.ui.main.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.core.main.credentials.Credentials
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewCredentialsBinding


class CredentialsViewHolder private constructor(val binding: CustomViewRecyclerviewCredentialsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        clickListener: ClickListener,
        optionsClickListener: OptionsClickListener?,
        credential: Credentials?,
        isSimplified: Boolean
    ) {
        binding.credential = credential
        binding.clickListener = clickListener
        binding.optionsClickListener = optionsClickListener
        binding.isSimplifiedVersion = isSimplified
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): CredentialsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                CustomViewRecyclerviewCredentialsBinding.inflate(layoutInflater, parent, false)
            return CredentialsViewHolder(
                binding
            )
        }
    }
}

class ClickListener(val clickListener: (credential: Credentials) -> Unit) {
    fun onClick(credential: Credentials) = clickListener(credential)
}

class OptionsClickListener(val clickListener: (view: View, credential: Credentials) -> Unit) {
    fun onClick(view: View, credential: Credentials) = clickListener(view, credential)
}