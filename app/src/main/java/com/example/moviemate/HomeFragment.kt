package com.example.moviemate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class HomeFragment : Fragment() {

    private val apiKey: String = BuildConfig.API_KEY
    private lateinit var auth: FirebaseAuth
    private lateinit var tvHomeGreeting: TextView
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tvHomeGreeting = view.findViewById(R.id.tvHomeGreeting)

        // auth = (activity as? MainActivity)?.auth ?: FirebaseAuth.getInstance()
        // Fancy version feat. Typecasting, null safety OP. and elvis OP.
        // Me being a rebel, let's ignore it :P
        auth = (activity as MainActivity).auth

        requestQueue = Volley.newRequestQueue(requireContext())
        populateHomeScreen(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        // Set greeting for user on home fragment
        val nickname = auth.currentUser?.displayName
        if (nickname.toString() == "")
            tvHomeGreeting.text = "Adventurer"
        else
            tvHomeGreeting.text = nickname.toString()
    }


    private fun populateHomeScreen(view: View) {
        // TRENDING MOVIES
        getTrending(requestQueue, apiKey) { movieList ->
            val rvTrending = view.findViewById<RecyclerView>(R.id.rvTrendingMovies)
            rvTrending.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvTrending.adapter = MovieCardAdapter(requestQueue, movieList)
        }

        // RECOMMENDED MOVIES
        getRecommendations(requestQueue, apiKey, 693134) { movieList ->
            val rvRecommends = view.findViewById<RecyclerView>(R.id.rvRecommendedMovies)
            rvRecommends.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvRecommends.adapter = MovieCardAdapter(requestQueue, movieList)
        }

        // TRENDING SHOWS
        getTrending(requestQueue, apiKey) { movieList ->
            val rvTrending = view.findViewById<RecyclerView>(R.id.rvTrendingShows)
            rvTrending.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvTrending.adapter = MovieCardAdapter(requestQueue, movieList)
        }

        // RECOMMENDED SHOWS
        getRecommendations(requestQueue, apiKey, 13183) { movieList ->
            val rvRecommends = view.findViewById<RecyclerView>(R.id.rvRecommendedShows)
            rvRecommends.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvRecommends.adapter = MovieCardAdapter(requestQueue, movieList)
        }
    }


}