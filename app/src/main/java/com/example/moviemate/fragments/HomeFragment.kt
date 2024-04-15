package com.example.moviemate.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.moviemate.BuildConfig
import com.example.moviemate.MainActivity
import com.example.moviemate.R
import com.example.moviemate.adapter.WatchlistMovieAdapter
import com.example.moviemate.api.getRecommendedMovies
import com.example.moviemate.api.getTrendingMovies
import com.example.moviemate.api.setImage
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private val apiKey: String = BuildConfig.API_KEY
    private lateinit var auth: FirebaseAuth
    private lateinit var tvHomeGreeting: TextView
    private lateinit var requestQueue: RequestQueue
    private lateinit var fragmentManager: FragmentManager
    private lateinit var ivHomeProfileImage: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tvHomeGreeting = view.findViewById(R.id.tvHomeGreeting)
        ivHomeProfileImage = view.findViewById(R.id.ivHomeProfileImage)
        tvHomeGreeting.setOnClickListener { navigateToProfile() }
        ivHomeProfileImage.setOnClickListener { navigateToProfile() }

        // auth = (activity as? MainActivity)?.auth ?: FirebaseAuth.getInstance()
        // Fancy version feat. Typecasting, null safety OP. and elvis OP.
        // Me being a rebel, let's ignore it :P
        auth = (activity as MainActivity).auth
        fragmentManager = (activity as MainActivity).supportFragmentManager

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


        var profileImageUrl = "https://api.dicebear.com/8.x/adventurer/png"
        profileImageUrl += "?backgroundColor=transparent"
        profileImageUrl += "&seed=${auth.currentUser!!.displayName}"
        setImage(requestQueue, profileImageUrl) {
            ivHomeProfileImage.setImageBitmap(it)
        }
    }


    private fun populateHomeScreen(view: View) {
        // TRENDING MOVIES
        getTrendingMovies(requestQueue, apiKey) { movieList ->
            val rvTrending = view.findViewById<RecyclerView>(R.id.rvTrendingMovies)
            rvTrending.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvTrending.adapter = WatchlistMovieAdapter(requestQueue, movieList, fragmentManager)
        }

        // RECOMMENDED MOVIES
        getRecommendedMovies(requestQueue, apiKey, 693134) { movieList ->
            val rvRecommends = view.findViewById<RecyclerView>(R.id.rvRecommendedMovies)
            rvRecommends.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvRecommends.adapter = WatchlistMovieAdapter(requestQueue, movieList, fragmentManager)
        }

        // TRENDING SHOWS
        getTrendingMovies(requestQueue, apiKey) { movieList ->
            val rvTrending = view.findViewById<RecyclerView>(R.id.rvTrendingShows)
            rvTrending.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvTrending.adapter = WatchlistMovieAdapter(requestQueue, movieList, fragmentManager)
        }

        // RECOMMENDED SHOWS
        getRecommendedMovies(requestQueue, apiKey, 13183) { movieList ->
            val rvRecommends = view.findViewById<RecyclerView>(R.id.rvRecommendedShows)
            rvRecommends.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvRecommends.adapter = WatchlistMovieAdapter(requestQueue, movieList, fragmentManager)
        }
    }


    private fun navigateToProfile() {
        fragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, ProfileFragment())
            .addToBackStack(null)
            .commit()
    }
}