package com.weminder

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.weminder.api.SocketHandler
import com.weminder.api.WEvent
import com.weminder.data.User
import com.weminder.databinding.ActivityDashboardBinding
import com.weminder.ui.login.LoginViewModel
import com.weminder.utils.AppUtils
import kotlinx.android.synthetic.main.nav_header_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDashboard.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Update username at the Drawer Header
        val navHeader = navView.getHeaderView(0)
        loginViewModel.user.observe(this, {
            navHeader.findViewById<TextView>(R.id.txtUsername).text = it.username
        })

        setupSocket()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_logout -> {
                AppUtils.updateUserId(this, "")
                loginViewModel.wipeDb()

                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.disconnect()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupSocket() {
        SocketHandler.initSocket(this)
        SocketHandler.subscribe(WEvent.ON_ME) { onMe(it) }
        SocketHandler.emit(WEvent.ME, null)
    }

    private fun onMe(arg: Array<Any>) {
        this.runOnUiThread {
            val user = SocketHandler.getDTO(User::class.java, arg)
            Toast.makeText(this, "Hello ${user.username}", Toast.LENGTH_SHORT).show()

            txtUsername.text = user.username
            SocketHandler.disconnect()
        }
    }

}