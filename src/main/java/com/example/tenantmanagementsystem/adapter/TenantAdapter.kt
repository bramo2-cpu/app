package com.example.tenantmanagementsystem.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tenantmanagementsystem.Tenant
import com.example.tenantmanagementsystem.databinding.ItemTenantBinding

class TenantAdapter : ListAdapter<Tenant, TenantAdapter.TenantViewHolder>(TenantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TenantViewHolder {
        val binding = ItemTenantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TenantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TenantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TenantViewHolder(private val binding: ItemTenantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tenant: Tenant) {
            binding.itemTenantName.text = tenant.name
            binding.itemTenantPhone.text = "Phone: ${tenant.phone}"
            binding.itemTenantRent.text = "Rent: ${tenant.formattedRent()}"
        }
    }

    class TenantDiffCallback : DiffUtil.ItemCallback<Tenant>() {
        override fun areItemsTheSame(oldItem: Tenant, newItem: Tenant): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tenant, newItem: Tenant): Boolean {
            return oldItem == newItem
        }
    }
}