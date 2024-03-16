package com.example.moviemate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class MovieCardModel(
    val name: String = "N/A",
    val rating: Double = 0.0,
)

class MovieCardVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //    val cmMovieImage: ImageView = itemView.findViewById(R.id.cmMovieImage)
    val cmMovieName: TextView = itemView.findViewById(R.id.cmMovieName)
}

class MovieCardAdapter(private val movieList: List<MovieCardModel>) :
    RecyclerView.Adapter<MovieCardVH>() {
    override fun getItemCount(): Int {
        return movieList.size
    }


    override fun onBindViewHolder(holder: MovieCardVH, position: Int) {
        val currentItem = movieList[position]
        holder.cmMovieName.text = currentItem.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_movie, parent, false)
        return MovieCardVH(view)
    }
}