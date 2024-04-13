package com.example.moviemate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatRatingBar
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.moviemate.api.getMovieDetails
import com.example.moviemate.api.setImage
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.CircularProgressIndicator


private const val imageBaseAPI = "https://image.tmdb.org/t/p"

// w45, w92, w154, w185, w300, w500, original
private const val posterSize = "w300"

// w300, w780, w1280, original
private const val backdropSize = "w780"

class MovieInfoFragment : Fragment() {
    private var movieID: Int = 0
    private val apiKey = BuildConfig.API_KEY
    private lateinit var requestQueue: RequestQueue
    private lateinit var movieInfoRootLayout: LinearLayout
    private lateinit var loadingSpinner: CircularProgressIndicator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movie_info, container, false)

        movieID = arguments?.getString("MovieID")!!.toInt()
        requestQueue = Volley.newRequestQueue(requireContext())
        loadingSpinner = view.findViewById(R.id.cpiMovieDetail)
        movieInfoRootLayout = view.findViewById(R.id.movieInfoRootLayout)


        getMovieDetails(requestQueue, apiKey, movieID) { movieDetails ->
            val tvMovieTitle: TextView = view.findViewById(R.id.tvMovieDetailMovieTitle)
            val ivMoviePoster: ImageView = view.findViewById(R.id.ivMovieDetailMoviePoster)
            val ivMovieBackdrop: ImageView = view.findViewById(R.id.ivMovieDetailMovieBackdrop)
            val tvMovieOverview: TextView = view.findViewById(R.id.tvMovieDetailMovieOverview)
            val rbMovieRating: AppCompatRatingBar = view.findViewById(R.id.rbMovieInfoMovieRating)
            val chipGroup: ChipGroup = view.findViewById(R.id.cgMovieDetailsGenres)

            val posterUrl = "$imageBaseAPI/$posterSize/${movieDetails.posterPath}"
            val backdropUrl = "$imageBaseAPI/$backdropSize/${movieDetails.backdropPath}"

            // Set poster and backdrop
            setImage(requestQueue, posterUrl) { posterBitmap ->
                ivMoviePoster.setImageBitmap(posterBitmap)
            }
            setImage(requestQueue, backdropUrl) { backdropBitmap ->
                ivMovieBackdrop.setImageBitmap(backdropBitmap)
            }

            // Set movie title, overview and rating
            tvMovieTitle.text = movieDetails.title
            tvMovieOverview.text = movieDetails.overview
            rbMovieRating.rating = (movieDetails.voteAverage / 2).toFloat()

            // Create and add chips for movie genre
            for (i in 0 until movieDetails.genres.size) {
                val chip = Chip(requireContext())
                chip.text = movieDetails.genres[i]
                chipGroup.addView(chip)
            }

            // Hide loading spinner and show root layout
            loadingSpinner.visibility = View.GONE
            movieInfoRootLayout.visibility = View.VISIBLE
        }

        return view
    }
}