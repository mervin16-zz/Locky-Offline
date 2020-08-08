package com.th3pl4gu3.locky_offline.ui.main.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.core.credentials.Credentials
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewCredentialsBinding

class CredentialsViewHolder private constructor(val binding: CustomViewRecyclerviewCredentialsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        credentialListener: CredentialListener,
        credential: Credentials?,
        isSimplified: Boolean
    ) {
        binding.listener = credentialListener
        binding.credential = credential
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

