package com.example.moviemate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.Toast

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvHomeGreeting: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tvHomeGreeting = view.findViewById(R.id.tvHomeGreeting)

        // Fancy version feat. Typecasting, null safety operator and elvis operator
        // Me being the rebel I am, let's ignore it :P
        // auth = (activity as? MainActivity)?.auth ?: FirebaseAuth.getInstance()
        auth = (activity as MainActivity).auth

        return view
    }


    override fun onResume() {
        super.onResume()
        setGreeting()
    }

    private fun setGreeting() {
        // Set greeting for user on home fragment
        val nickname = auth.currentUser?.displayName
        val greeting = "Welcome, " + nickname.toString() ?: "Adventurer"
        tvHomeGreeting.text = greeting
    }

}