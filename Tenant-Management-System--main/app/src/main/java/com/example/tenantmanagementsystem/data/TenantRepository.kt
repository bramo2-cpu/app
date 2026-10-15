package com.example.tenantmanagementsystem.data

import com.example.tenantmanagementsystem.Tenant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TenantRepository {
    private val _tenants = MutableStateFlow<List<Tenant>>(
        listOf(
            Tenant(name = "John Kamau", phone = "0712345678", rent = "25000"),
            Tenant(name = "Mary Wanjiku", phone = "0722987654", rent = "30000"),
            Tenant(name = "Peter Ochieng", phone = "0733112233", rent = "18000")
        )
    )
    val tenants: StateFlow<List<Tenant>> = _tenants.asStateFlow()

    fun addTenant(tenant: Tenant) {
        _tenants.value = _tenants.value + tenant
    }

    fun getTenantCount(): Int {
        return _tenants.value.size
    }

    fun getTotalRentCollected(): Double {
        return _tenants.value.sumOf { it.rent.toDoubleOrNull() ?: 0.0 }
    }
}