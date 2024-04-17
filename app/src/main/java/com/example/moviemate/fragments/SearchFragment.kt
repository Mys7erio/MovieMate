package com.example.moviemate.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.moviemate.R
import android.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.moviemate.BuildConfig
import com.example.moviemate.MainActivity
import com.example.moviemate.adapter.MovieAdapter
import com.example.moviemate.adapter.MovieModel
import com.example.moviemate.api.searchMovies
import com.google.android.material.progressindicator.CircularProgressIndicator

class SearchFragment : Fragment() {

    private val apiKey = BuildConfig.API_KEY
    private var movieList = arrayListOf<MovieModel>()
    private lateinit var searchBar: SearchView
    private lateinit var requestQueue: RequestQueue
    private lateinit var rvSearchFragment: RecyclerView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var searchFragRootLayout: LinearLayout
    private lateinit var cpiSearch: CircularProgressIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchBar = view.findViewById(R.id.searchBarNew)
        requestQueue = Volley.newRequestQueue(requireContext())
        fragmentManager = (activity as MainActivity).supportFragmentManager

        cpiSearch = view.findViewById(R.id.cpiSearch)
        searchFragRootLayout = view.findViewById(R.id.searchFragRootLayout)

        rvSearchFragment = view.findViewById(R.id.rvSearchFragment)
        rvSearchFragment.layoutManager = GridLayoutManager(requireContext(), 3)
        rvSearchFragment.adapter = MovieAdapter(requestQueue, movieList, fragmentManager)

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // handle query text changed
            override fun onQueryTextChange(newText: String?) = true

            override fun onQueryTextSubmit(query: String?): Boolean {
                // Hide layout and show progress indicator
                searchFragRootLayout.visibility = View.GONE
                cpiSearch.visibility = View.VISIBLE

                // Make http request
                searchMovies(requestQueue, apiKey, query.toString()) { movieList ->
                    (rvSearchFragment.adapter as? MovieAdapter)?.setMovieList(movieList)

                    // Show search fragment root layout again and hide progress bar
                    searchFragRootLayout.visibility = View.VISIBLE
                    cpiSearch.visibility = View.GONE
                }
                return true
            }

        })
        return view
    }
}