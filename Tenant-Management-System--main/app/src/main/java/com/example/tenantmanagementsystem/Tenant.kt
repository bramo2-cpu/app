package com.example.tenantmanagementsystem

import java.io.Serializable
import java.util.UUID

data class Tenant(
    val name: String = "",
    val phone: String = "",
    val rent: String = "",
    val id: String = UUID.randomUUID().toString()
) : Serializable {
    fun summary(): String {
        return "Tenant: $name\nPhone: $phone\nRent paid: KSh $rent"
    }

    fun formattedRent(): String {
        return "KSh $rent"
    }
}
