package com.example.moviemate.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviemate.R

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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