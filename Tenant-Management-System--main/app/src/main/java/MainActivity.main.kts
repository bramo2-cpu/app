package com.example.tenantmanagementsystem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tenantmanagementsystem.databinding.ActivityMainBinding
import com.example.tenantmanagementsystem.databinding.NavHeaderBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastTenant: Tenant? = null
    private var saveCount = 0
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TENANT_LIFECYCLE", "MainActivity: onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Welcome user from Login
        val userEmail = intent.getStringExtra("EMAIL")
        if (userEmail != null) {
            Toast.makeText(this, "Logged in as $userEmail", Toast.LENGTH_SHORT).show()
        }

        // ── RESTORE STATE (only after a rotation) ─────────────────
        if (savedInstanceState != null) {
            val name = savedInstanceState.getString("tenant_name")
            val phone = savedInstanceState.getString("tenant_phone")
            val rent = savedInstanceState.getString("tenant_rent")
            if (name != null && phone != null && rent != null) {
                val tenant = Tenant(name, phone, rent)
                lastTenant = tenant
                binding.tenant = tenant
            }
            saveCount = savedInstanceState.getInt("save_count", 0)
            if (saveCount > 0) {
                Toast.makeText(this, "SAVE pressed $saveCount time(s) before rotation", Toast.LENGTH_SHORT).show()
                // 1. Use Toolbar as App Bar
                setSupportActionBar(binding.toolbar)

                // 2. Get NavController from NavHost
                val navHostFragment = supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                navController = navHostFragment.navController

                // 3. Top-level destinations + drawer
                appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.dashboardFragment,
                        R.id.addTenantFragment,
                        R.id.viewTenantsFragment,
                        R.id.aboutFragment
                    ),
                    binding.drawerLayout
                )

                // 4. Connect App Bar and Drawer to navigation
                setupActionBarWithNavController(navController, appBarConfiguration)
                binding.navView.setupWithNavController(navController)

                // Try it yourself #4: Custom navigation listener to handle drawer logout
                binding.navView.setNavigationItemSelectedListener { item ->
                    if (item.itemId == R.id.action_drawer_logout) {
                        binding.drawerLayout.closeDrawers()
                        performLogout()
                        true
                    } else {
                        val handled = item.onNavDestinationSelected(navController)
                        if (handled) {
                            binding.drawerLayout.closeDrawers()
                        }
                        handled
                    }
                }

                binding.saveButton.setOnClickListener {
                    val name = binding.tenantNameEditText.text.toString().trim()
                    val phone = binding.phoneEditText.text.toString().trim()
                    val rent = binding.rentEditText.text.toString().trim()

                    var hasError = false
                    if (name.isEmpty()) {
                        binding.tenantNameEditText.error = "Required"
                        hasError = true
                    }
                    if (phone.isEmpty()) {
                        binding.phoneEditText.error = "Required"
                        hasError = true
                    }
                    if (rent.isEmpty()) {
                        binding.rentEditText.error = "Required"
                        hasError = true
                    }
                    if (hasError) return@setOnClickListener

                    saveCount++
                    val tenant = Tenant(name, phone, rent)
                    binding.tenant = tenant
                    lastTenant = tenant
                    TenantRepository.addTenant(tenant)
                    // 5. Show user's email in drawer header
                    val email = intent.getStringExtra("EMAIL")
                    val headerBinding = NavHeaderBinding.bind(binding.navView.getHeaderView(0))
                    headerBinding.headerEmailTextView.text = email ?: "Signed in"
                }

                binding.tenantNameEditText.text?.clear()
                binding.phoneEditText.text?.clear()
                binding.rentEditText.text?.clear()
            }
            private fun performLogout() {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

            binding.callButton.setOnClickListener {
                val tenant = lastTenant
                if (tenant == null) {
                    Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${tenant.phone}"))
                startActivity(intent)
            }
            // ☰ opens the drawer; ← goes back
            override fun onSupportNavigateUp(): Boolean {
                return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
            }

            binding.shareButton.setOnClickListener {
                val tenant = lastTenant
                if (tenant == null) {
                    Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, tenant.summary())
                startActivity(Intent.createChooser(shareIntent, "Share tenant"))
            }
            // Turn main_menu.xml into menu items in the App Bar
            override fun onCreateOptionsMenu(menu: Menu): Boolean {
                menuInflater.inflate(R.menu.main_menu, menu)
                return true
            }

            // ── SAVE STATE (just before the Activity is destroyed) ────────
            override fun onSaveInstanceState(outState: Bundle) {
                super.onSaveInstanceState(outState)
                Log.d("TENANT_LIFECYCLE", "MainActivity: onSaveInstanceState")
                val tenant = lastTenant
                if (tenant != null) {
                    outState.putString("tenant_name", tenant.name)
                    outState.putString("tenant_phone", tenant.phone)
                    outState.putString("tenant_rent", tenant.rent)
                    // What happens when a menu item is tapped
                    override fun onOptionsItemSelected(item: MenuItem): Boolean {
                        if (item.itemId == R.id.action_logout) {
                            performLogout()
                            return true
                        }
                        outState.putInt("save_count", saveCount)
                        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
                    }

                    // ── LIFECYCLE METHODS ─────────────────────────────────────────
                    // ── LIFECYCLE LOGS ────────────────────────────────────────────
                    override fun onStart() {
                        super.onStart()
                        Log.d("TENANT_LIFECYCLE", "MainActivity: onStart")