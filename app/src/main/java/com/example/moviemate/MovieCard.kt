package com.example.moviemate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


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
    //    val cmMovieImage: ImageView = itemView.findViewById(R.id.cmMovieImage)
    val cmMovieName: TextView = itemView.findViewById(R.id.cmMovieName)
}

class MovieCardAdapter(private val movieList: ArrayList<MovieModel>) :
    RecyclerView.Adapter<MovieCardVH>() {
    override fun getItemCount(): Int {
        return movieList.size
    }


    override fun onBindViewHolder(holder: MovieCardVH, position: Int) {
        val currentItem = movieList[position]
        holder.cmMovieName.text = currentItem.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_movie, parent, false)
        return MovieCardVH(view)
    }

}