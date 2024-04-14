package com.example.moviemate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatRatingBar
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.moviemate.api.getDocRefToUserCol
import com.example.moviemate.api.getMovieDetails
import com.example.moviemate.api.isMovieInUserCol
import com.example.moviemate.api.setImage
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val imageBaseAPI = "https://image.tmdb.org/t/p"

// w45, w92, w154, w185, w300, w500, original
private const val posterSize = "w300"

// w300, w780, w1280, original
private const val backdropSize = "w780"

class MovieInfoFragment : Fragment() {
    private val apiKey = BuildConfig.API_KEY
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var requestQueue: RequestQueue
    private lateinit var movieInfoRootLayout: LinearLayout
    private lateinit var loadingSpinner: CircularProgressIndicator

    private var movieID = 0
    private var posterUrl = ""
    private var backdropUrl = ""
    private var movieInWatchlist = false
    private var movieInNotifList = false
    private var movieAlreadyWatched = false

    private lateinit var btnWatchlist: ImageButton
    private lateinit var btnDoneWatching: ImageButton
    private lateinit var btnNotify: ImageButton

    private lateinit var tvNotifyLbl: TextView
    private lateinit var tvWatchlistLbl: TextView
    private lateinit var tvDoneWatchingLbl: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movie_info, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        movieID = arguments?.getString("MovieID")!!.toInt()
        requestQueue = Volley.newRequestQueue(requireContext())
        loadingSpinner = view.findViewById(R.id.cpiMovieDetail)
        movieInfoRootLayout = view.findViewById(R.id.movieInfoRootLayout)

        tvNotifyLbl = view.findViewById(R.id.tvMovieInfoNotify)
        tvWatchlistLbl = view.findViewById(R.id.tvMovieInfoWatchlist)
        tvDoneWatchingLbl = view.findViewById(R.id.tvMovieInfoDoneWatching)

        btnWatchlist = view.findViewById(R.id.btnWatchlist)
        btnDoneWatching = view.findViewById(R.id.btnMarkWatched)
        btnNotify = view.findViewById(R.id.btnNotify)

        // Run functions to check which collections the movies are in
        CoroutineScope(Dispatchers.Main).launch {
            movieInWatchlist = isMovieInUserCol(auth, db, "watchlist", movieID)
            movieAlreadyWatched = isMovieInUserCol(auth, db, "doneWatching", movieID)
            modifyBtnStates()
        }

        getMovieDetails(requestQueue, apiKey, movieID) { movieDetails ->

            btnDoneWatching.setOnClickListener { handleBtnDoneWatchingClicked() }
            btnWatchlist.setOnClickListener { handleBtnWatchlistClicked() }
            btnNotify.setOnClickListener { handleBtnNotifyClicked() }

            val tvMovieTitle: TextView = view.findViewById(R.id.tvMovieDetailMovieTitle)
            val ivMoviePoster: ImageView = view.findViewById(R.id.ivMovieDetailMoviePoster)
            val ivMovieBackdrop: ImageView = view.findViewById(R.id.ivMovieDetailMovieBackdrop)
            val tvMovieOverview: TextView = view.findViewById(R.id.tvMovieDetailMovieOverview)
            val rbMovieRating: AppCompatRatingBar = view.findViewById(R.id.rbMovieInfoMovieRating)
            val chipGroup: ChipGroup = view.findViewById(R.id.cgMovieDetailsGenres)

            posterUrl = "$imageBaseAPI/$posterSize${movieDetails.posterPath}"
            backdropUrl = "$imageBaseAPI/$backdropSize${movieDetails.backdropPath}"

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


    private fun modifyBtnStates() {
        // Set icon and text for Watchlist
        tvWatchlistLbl.text = if (movieInWatchlist) "Remove from Watchlist" else "Save to Watchlist"
        btnWatchlist.setImageResource(
            if (movieInWatchlist) R.drawable.ic_bookmark_filled
            else R.drawable.ic_bookmark_outlined
        )

        // Set icon and text for Done Watching
        tvDoneWatchingLbl.text = if (movieAlreadyWatched) "Mark as Unwatched" else "Mark as Watched"
        btnDoneWatching.setImageResource(
            if (movieAlreadyWatched) R.drawable.ic_check_circle_filled
            else R.drawable.ic_check_circle_outline
        ) // @formatter:on


        // Set icon and text for Notifications
        tvNotifyLbl.text = if (movieInNotifList) "Cancel Notifications" else "Notify"
        btnNotify.setImageResource(
            if (movieInNotifList) R.drawable.ic_notification_circle_filled
            else R.drawable.ic_notification_circle_outlined
        )
    }

    private fun handleBtnWatchlistClicked() {
        val docRef = getDocRefToUserCol(auth, db, "watchlist", movieID)
        val movieData = hashMapOf(
            "poster_path" to posterUrl,
            "backdrop_path" to backdropUrl
        )

        // Add movie to watchlist if it's not there, otherwise delete it
        if (movieInWatchlist) docRef.delete()
        else docRef.set(movieData)

        // Update visual state of the button
        movieInWatchlist = !movieInWatchlist
        modifyBtnStates()
    }

    private fun handleBtnDoneWatchingClicked() {
        val docRef = getDocRefToUserCol(auth, db, "doneWatching", movieID)
        val movieData = hashMapOf(
            "poster_path" to posterUrl,
            "backdrop_path" to backdropUrl
        )

        // Add movie to watchlist if it's not there, otherwise delete it
        if (movieAlreadyWatched) docRef.delete()
        else docRef.set(movieData)

        // Update visual state of the button
        movieAlreadyWatched = !movieAlreadyWatched
        modifyBtnStates()
    }

    private fun handleBtnNotifyClicked() {
        movieInNotifList = !movieInNotifList
        modifyBtnStates()
    }
}