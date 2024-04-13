package com.example.moviemate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.example.moviemate.api.createErrorListener


private const val imageBaseAPI = "https://image.tmdb.org/t/p"

// w45, w92, w154, w185, w300, w500, original
private const val imageSize = "w300"

data class MovieModel(
    val id: Int,
    val title: String = "N/A",
    val overview: String = "",
    val voteAverage: Double = 0.0,

    val releaseDate: String = "01-01-1970",
    var genres: MutableList<String> = mutableListOf(),
    val posterPath: String = "",
    val backdropPath: String = "",
)

class MovieCardVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardPoster: ImageView = itemView.findViewById(R.id.cardPoster)
    var id: Int = 0
    var title: String = ""
    var overview: String = ""
    var voteAverage: Double? = null
    var releaseDate: String = ""
    var genres: List<String> = emptyList()
    var posterPath: String = ""
    var backdropPath: String = ""
}

class MovieCardAdapter(
    private val requestQueue: RequestQueue,
    private val movieList: ArrayList<MovieModel>,
    private val fragmentManager: FragmentManager

) :
    RecyclerView.Adapter<MovieCardVH>() {
    override fun getItemCount(): Int {
        return movieList.size
    }

    private fun setCardPoster(holder: MovieCardVH, url: String) {
        val request = ImageRequest(
            url,
            { posterBitmap ->
                holder.cardPoster.setImageBitmap(posterBitmap)
            }, 0, 0, null, null,

            createErrorListener() // Handle Errors
        )

        requestQueue.add(request)
        Log.e("MOVIEMATE", "FETCHED POSTER: $url")
    }


    override fun onBindViewHolder(holder: MovieCardVH, position: Int) {
        val currentItem = movieList[position]
        setCardPoster(holder, "$imageBaseAPI/$imageSize/${currentItem.posterPath}")
        holder.id = currentItem.id
        holder.title = currentItem.title
        holder.overview = currentItem.overview
        holder.voteAverage = currentItem.voteAverage
        holder.releaseDate = currentItem.releaseDate
        holder.genres = currentItem.genres
        holder.posterPath = currentItem.posterPath
        holder.backdropPath = currentItem.backdropPath
        holder.itemView.setOnClickListener {
            Log.e("MOVIEMATE", "CLICKED: ${holder.title}")
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
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_movie, parent, false)
        return MovieCardVH(view)
    }

}