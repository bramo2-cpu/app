package com.example.tenantmanagementsystem

import java.util.UUID

data class Tenant(
    val name: String = "",
    val phone: String = "",
    val rent: String = "",
    val id: String = UUID.randomUUID().toString()
) {
    fun summary(): String {
        return "Tenant: $name\nPhone: $phone\nRent paid: KSh $rent"
    }

    fun formattedRent(): String {
        return "KSh $rent"
    }
}
