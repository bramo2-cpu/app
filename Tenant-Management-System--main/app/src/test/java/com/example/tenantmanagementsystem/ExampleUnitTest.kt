package com.example.tenantmanagementsystem

import com.example.tenantmanagementsystem.viewmodel.TenantViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var viewModel: TenantViewModel

    @Before
    fun setUp() {
        viewModel = TenantViewModel()
    }

    @Test
    fun testTenantSummaryFormat() {
        val tenant = Tenant(name = "Alice", phone = "0711223344", rent = "15000")
        val expected = "Tenant: Alice\nPhone: 0711223344\nRent paid: KSh 15000"
        assertEquals(expected, tenant.summary())
    }

    @Test
    fun testTenantFormattedRent() {
        val tenant = Tenant(rent = "20000")
        assertEquals("KSh 20000", tenant.formattedRent())
    }

    @Test
    fun testAddTenantIncreasesCount() {
        val initialCount = viewModel.getTotalTenants()
        viewModel.addTenant("Bob Smith", "0799887766", "12000")
        val newCount = viewModel.getTotalTenants()
        assertEquals(initialCount + 1, newCount)
    }

    @Test
    fun testTotalRentCalculation() {
        val tenantList = viewModel.tenants.value
        val expectedTotal = tenantList.sumOf { it.rent.toDoubleOrNull() ?: 0.0 }
        val formattedExpected = "KSh %.2f".format(expectedTotal)
        assertEquals(formattedExpected, viewModel.getTotalRentCollected())
    }
}