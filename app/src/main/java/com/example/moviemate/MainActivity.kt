package com.example.moviemate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import android.view.MenuItem
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationBar: BottomNavigationView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set initial fragment
        replaceFragment(HomeFragment())

        // Initialize firebase auth
        auth = FirebaseAuth.getInstance()

        bottomNavigationBar = findViewById(R.id.bottomNavView)
        bottomNavigationBar.setOnItemSelectedListener { menuItem ->
            handleNavigation(menuItem)
        }

        if (auth.currentUser == null) {
            Toast.makeText(this, "Please Login...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleNavigation(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.navitem_home -> {
                replaceFragment(HomeFragment())
                return true
            }

            R.id.navitem_profile -> {
                replaceFragment(ProfileFragment())
                return true
            }

            R.id.navitem_watchlist -> {
                replaceFragment(WatchlistFragment())
                return true
            }

            R.id.navitem_watched -> {
                replaceFragment(WatchedFragment())
                return true
            }

            else -> {
                Toast.makeText(applicationContext, "Invalid Option Selected", Toast.LENGTH_LONG)
                    .show()
                return false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }
}




