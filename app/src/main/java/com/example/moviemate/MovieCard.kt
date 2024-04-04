package com.example.moviemate

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest


private const val imageBaseAPI = "https://image.tmdb.org/t/p"
private const val imageSize = "w300"   // w45, w92, w154, w185, w300, w500, original

data class MovieModel(
    val id: String,
    val title: String = "N/A",
    val description: String = "",
    val voteAverage: Double = 0.0,

    val releaseDate: String = "01-01-1970",
    val genres: List<String> = listOf(),
    val posterPath: String = "",
    val backdropPath: String = "",
)

class MovieCardVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardPoster: ImageView = itemView.findViewById(R.id.cardPoster)
    var id: String = ""
    var title: String = ""
    var description: String = ""
    var voteAverage: Double? = null
    var releaseDate: String = ""
    var genres: List<String> = emptyList()
    var posterPath: String = ""
    var backdropPath: String = ""


    init {
        // Add click listener to the itemView (the whole card)
        itemView.setOnClickListener {
            val movieName = title // Assuming title is the movie name
            Toast.makeText(itemView.context, movieName, Toast.LENGTH_SHORT).show()
        }
    }
}

class MovieCardAdapter(
    private val requestQueue: RequestQueue,
    private val movieList: ArrayList<MovieModel>
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
        holder.description = currentItem.description
        holder.voteAverage = currentItem.voteAverage
        holder.releaseDate = currentItem.releaseDate
        holder.genres = currentItem.genres
        holder.posterPath = currentItem.posterPath
        holder.backdropPath = currentItem.backdropPath
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_movie, parent, false)
        return MovieCardVH(view)
    }

}