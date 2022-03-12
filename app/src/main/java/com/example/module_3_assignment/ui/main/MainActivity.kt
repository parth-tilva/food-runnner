package com.example.module_3_assignment .ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.module_3_assignment.R
import com.example.module_3_assignment.ui.login.LogInActivity
import com.example.module_3_assignment.ui.login.RegisterActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var textView: TextView
    lateinit var drawerLayout: DrawerLayout
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        drawerLayout = findViewById(R.id.drawer_layout)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<NavigationView>(R.id.navigation_view)
            .setupWithNavController(navController)

         appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    fun shutdown(item: MenuItem) {
        if(item.itemId==R.id.log_out){
            Log.d("main","lout out success")
            sharedPreferences.edit().putBoolean("IsSignedIN",false).apply()
            val intent = Intent(this@MainActivity,LogInActivity::class.java)
            startActivity(intent)
        }
    }
}

//        val isSignedIn = sharedPreferences.getBoolean("IsSignedIN",false)