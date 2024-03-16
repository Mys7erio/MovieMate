package com.example.moviemate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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


        val movieList = mutableListOf(
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
            MovieCardModel("Edge of Tomorrow", 8.5),
        )

        val rvHome = view.findViewById<RecyclerView>(R.id.rvHome)
        rvHome.layoutManager = LinearLayoutManager(requireContext())
        rvHome.adapter = MovieCardAdapter(movieList)

        return view
    }


    override fun onResume() {
        super.onResume()
        setGreeting()
    }

    private fun setGreeting() {
        // Set greeting for user on home fragment
        val nickname = auth.currentUser?.displayName
        val greeting = ("Welcome, " + nickname.toString()) ?: "Adventurer"
        tvHomeGreeting.text = greeting
    }

}