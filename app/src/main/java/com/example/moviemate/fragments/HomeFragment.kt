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
import com.example.moviemate.adapter.MovieAdapter
import com.example.moviemate.api.getRecommendedMovies
import com.example.moviemate.api.getTrendingMovies
import com.example.moviemate.api.setImage
import com.example.moviemate.utils.RECOMMENDATION_HISTORY_LIMIT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class HomeFragment : Fragment() {

    private val apiKey: String = BuildConfig.API_KEY
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var previouslyWatched: ArrayList<Int>
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
        db = FirebaseFirestore.getInstance()
        fragmentManager = (activity as MainActivity).supportFragmentManager

        CoroutineScope(Dispatchers.IO).launch {
            previouslyWatched = getPreviouslyWatched()
            populateHomeScreen(view)
        }

        requestQueue = Volley.newRequestQueue(requireContext())

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
        var channels = ArrayList<Int>()
        try {
            var lastTenWatched = previouslyWatched.takeLast(RECOMMENDATION_HISTORY_LIMIT)
            lastTenWatched = ArrayList<Int>(lastTenWatched)
            channels = getRandomFromArray(3, lastTenWatched)
        } catch (e: Exception) {
            channels = arrayListOf(27, 11, 2002)
        }

        // TRENDING MOVIES
        getTrendingMovies(requestQueue, apiKey) { movieList ->
            val rvTrending = view.findViewById<RecyclerView>(R.id.rvTrendingMovies)
            rvTrending.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvTrending.adapter = MovieAdapter(requestQueue, movieList, fragmentManager)
        }

        // RECOMMENDED MOVIES
        getRecommendedMovies(requestQueue, apiKey, channels[0]) { movieList ->
            val rvRecommends = view.findViewById<RecyclerView>(R.id.rvRecommendedMovies)
            rvRecommends.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvRecommends.adapter = MovieAdapter(requestQueue, movieList, fragmentManager)
        }

        // TRENDING SHOWS
        getRecommendedMovies(requestQueue, apiKey, channels[1]) { movieList ->
            val rvTrending = view.findViewById<RecyclerView>(R.id.rvTrendingShows)
            rvTrending.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvTrending.adapter = MovieAdapter(requestQueue, movieList, fragmentManager)
        }

        // RECOMMENDED SHOWS
        getRecommendedMovies(requestQueue, apiKey, channels[2]) { movieList ->
            val rvRecommends = view.findViewById<RecyclerView>(R.id.rvRecommendedShows)
            rvRecommends.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
            rvRecommends.adapter = MovieAdapter(requestQueue, movieList, fragmentManager)
        }
    }


    private fun navigateToProfile() {
        fragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, ProfileFragment())
            .addToBackStack(null)
            .commit()
    }


    private suspend fun getPreviouslyWatched(): ArrayList<Int> {
        val previouslyWatched = arrayListOf<Int>()
        val userEmail = auth.currentUser!!.email.toString()
        val collectionSnapshot = db.collection("users")
            .document(userEmail)
            .collection("doneWatching")
            .get()
            .await()

        for (document in collectionSnapshot.documents) {
            previouslyWatched.add(document.id.toInt())
        }

        return previouslyWatched
    }


    private fun getRandomFromArray(numItems: Int, array: ArrayList<Int>): ArrayList<Int> {
        val randoms = arrayListOf<Int>()
        for (i in 0..numItems) {
            val index = Random.nextInt(0, (array.size - 1))
            randoms.add(array[index])
            array.remove(index)
        }

        return randoms
    }
}