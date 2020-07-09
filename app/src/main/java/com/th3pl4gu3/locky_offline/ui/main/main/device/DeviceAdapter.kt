package com.th3pl4gu3.locky_offline.ui.main.main.device

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.th3pl4gu3.locky_offline.core.main.Device
import com.th3pl4gu3.locky_offline.databinding.CustomViewRecyclerviewDeviceBinding

class DeviceAdapter(
    private val clickListener: ClickListener,
    private val optionsClickListener: OptionsClickListener?,
    private val isSimplified: Boolean
) : ListAdapter<Device, DeviceAdapter.ViewHolder>(
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

    class ViewHolder private constructor(val binding: CustomViewRecyclerviewDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: ClickListener,
            optionsClickListener: OptionsClickListener?,
            device: Device,
            isSimplified: Boolean
        ) {
            binding.device = device
            binding.clickListener = clickListener
            binding.optionsClickListener = optionsClickListener
            binding.isSimplifiedVersion = isSimplified
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    CustomViewRecyclerviewDeviceBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Device>() {

    override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean {
        return oldItem == newItem
    }

}

class ClickListener(val clickListener: (device: Device) -> Unit) {
    fun onClick(device: Device) = clickListener(device)
}

class OptionsClickListener(val clickListener: (view: View, device: Device) -> Unit) {
    fun onClick(view: View, device: Device) = clickListener(view, device)
}