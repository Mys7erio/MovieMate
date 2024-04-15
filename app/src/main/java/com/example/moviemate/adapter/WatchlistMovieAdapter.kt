package com.example.moviemate.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.example.moviemate.utils.IMAGE_BASE_API
import com.example.moviemate.fragments.MovieInfoFragment
import com.example.moviemate.R
import com.example.moviemate.api.createErrorListener
import com.example.moviemate.utils.POSTER_SIZE

class WatchlistMovieAdapter(
    private val requestQueue: RequestQueue,
    private val movieList: ArrayList<MovieModel>,
    private val fragmentManager: FragmentManager

) :
    RecyclerView.Adapter<MovieViewHolder>() {
    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = movieList[position]
        setCardPoster(holder, "$IMAGE_BASE_API/$POSTER_SIZE/${currentItem.posterPath}")
        holder.id = currentItem.id
        holder.posterPath = currentItem.posterPath
        holder.backdropPath = currentItem.backdropPath
        holder.itemView.setOnClickListener { navigateToMovieInfo(holder) }
    }

    private fun setCardPoster(holder: MovieViewHolder, url: String) {
        val request = ImageRequest(
            url, { posterBitmap ->
                holder.cardPoster.setImageBitmap(posterBitmap)
            }, 0, 0, null, null,
            createErrorListener() // Handle Errors
        )
        requestQueue.add(request)
        Log.i("MOVIEMATE", "FETCHED POSTER: $url")
    }

    private fun navigateToMovieInfo(holder: MovieViewHolder) {
        Log.i("MOVIEMATE", "CLICKED: ${holder.title}")
        val movieInfoFragment = MovieInfoFragment()
        movieInfoFragment.arguments = Bundle().apply {
            putString("MovieID", holder.id.toString())
        }
        fragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, movieInfoFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_movie, parent, false)
        return MovieViewHolder(view)
    }
}