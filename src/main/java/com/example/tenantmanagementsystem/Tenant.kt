package com.example.tenantmanagementsystem

import java.util.UUID

data class Tenant(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val phone: String = "",
    val rent: String = "",
) {
    fun summary(): String {
        return "Tenant: $name\nPhone: $phone\nRent paid: KSh $rent"
    }

    fun formattedRent(): String {
        return "KSh $rent"
    }
}