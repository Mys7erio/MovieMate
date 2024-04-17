package com.example.moviemate.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.example.moviemate.MainActivity
import com.example.moviemate.R
import com.example.moviemate.adapter.MovieAdapter
import com.example.moviemate.adapter.MovieModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WatchedFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var fragmentManager: FragmentManager
    private var movieList: ArrayList<MovieModel> = arrayListOf()

    private lateinit var doneWatchingRootLayout: LinearLayout
    private lateinit var cpbDoneWatching: CircularProgressIndicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_watched, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        fragmentManager = (activity as MainActivity).supportFragmentManager
        cpbDoneWatching = view.findViewById(R.id.cpbDoneWatching)
        doneWatchingRootLayout = view.findViewById(R.id.doneWatchingRootLayout)

        val requestQueue = Volley.newRequestQueue(requireContext())

        val userEmail = auth.currentUser!!.email.toString()
        db.collection("users")
            .document(userEmail)
            .collection("doneWatching")
            .get()
            .addOnSuccessListener { snapshot ->
                // TODO: INVESTIGATE WHY THE NUMBER OF ITEMS INCREASE EVERYTIME THE FRAGMENT IS VISIBLE
                movieList.clear()
                for (doc in snapshot) {
                    val movie = MovieModel(
                        id = doc.id.toInt(),
                        posterPath = doc.getString("poster_path").toString(),
                        backdropPath = doc.getString("backdrop_path").toString()
                    )
                    Log.e("MOVIEMATE", doc.getString("posterPath").toString())
                    movieList.add(movie)
                }

                Log.e("doneWatching", movieList.toString())
                val rvDoneWatching = view.findViewById<RecyclerView>(R.id.rvDoneWatching)
                val layoutManager = GridLayoutManager(requireContext(), 3)
                rvDoneWatching.layoutManager = layoutManager
                rvDoneWatching.adapter = MovieAdapter(requestQueue, movieList, fragmentManager)


                // Hide progressbar and show Root layout for doneWatching Fragment
                cpbDoneWatching.visibility = View.GONE
                doneWatchingRootLayout.visibility = View.VISIBLE
            }

        return view
    }
}