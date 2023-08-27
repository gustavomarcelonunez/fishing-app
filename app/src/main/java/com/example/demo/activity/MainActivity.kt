package com.example.demo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.demo.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp

private lateinit var binding: ActivityMainBinding
private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var drawerLayout: DrawerLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        drawerLayout = binding.drawerLayout

        val navHostFragment =
            supportFragmentManager.findFragmentById(com.example.demo.R.id.theNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val extras = intent.getStringExtra("KEY")
        if (extras != null && extras == "NOTIFICATION") {
            val notificationBody = intent.getStringExtra("body1")
            val notificationTitle = intent.getStringExtra("title")

            val bundle = Bundle().apply {
                putString("notificationTitle", notificationTitle)
                putString("notificationBody", notificationBody)
            }

            val destinationId = com.example.demo.R.id.show_message_fragment
            navController.navigate(destinationId, bundle)

        }

        NavigationUI.setupWithNavController(binding.navView, navController)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        setContentView(view)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(com.example.demo.R.id.theNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}