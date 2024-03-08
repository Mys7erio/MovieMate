package com.example.moviemate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(HomeFragment())

        bottomNavigationBar = findViewById(R.id.bottomNavView)
        bottomNavigationBar.setOnItemSelectedListener() { menuItem ->
            when (menuItem.itemId) {
                R.id.navitem_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.navitem_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }

                R.id.navitem_watchlist -> {
                    replaceFragment(WatchlistFragment())
                    true
                }

                R.id.navitem_watched -> {
                    replaceFragment(WatchedFragment())
                    true
                }

                else -> {
                    Toast.makeText(applicationContext, "Invalid Option Selected", Toast.LENGTH_LONG).show()
                    false
                }
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




