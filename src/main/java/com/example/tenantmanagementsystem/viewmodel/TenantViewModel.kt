package com.example.tenantmanagementsystem.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tenantmanagementsystem.Tenant
import com.example.tenantmanagementsystem.data.TenantRepository
import kotlinx.coroutines.flow.StateFlow

class TenantViewModel : ViewModel() {
    val tenants: StateFlow<List<Tenant>> = TenantRepository.tenants

    fun addTenant(name: String, phone: String, rent: String) {
        val tenant = Tenant(name = name, phone = phone, rent = rent)
        TenantRepository.addTenant(tenant)
    }

    fun getTotalTenants(): Int {
        return TenantRepository.getTenantCount()
    }

    fun getTotalRentCollected(): String {
        val total = TenantRepository.getTotalRentCollected()
        return "KSh %.2f".format(total)
    }
}