package com.example.moviemate.adapter

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