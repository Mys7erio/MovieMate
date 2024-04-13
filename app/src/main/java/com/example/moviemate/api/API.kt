package com.example.moviemate.api

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.moviemate.MovieModel


fun getTrendingMovies(
    queue: RequestQueue,
    apiKey: String,
    callback: (ArrayList<MovieModel>) -> Unit // Callback parameter
): ArrayList<MovieModel> {
    val url = "https://api.themoviedb.org/3/movie/popular"
    val moviesList = ArrayList<MovieModel>()

    val request = object :
        JsonObjectRequest(
            Method.GET, url, null,

            // RUN CODE ON SUCCESS
            Response.Listener { response ->
                val resultsArray = response.getJSONArray("results")
                for (i in 0 until resultsArray.length()) {
                    val movieObject = resultsArray.getJSONObject(i)
                    val movie = getParsedMovieModel(movieObject)
                    moviesList.add(movie)
                }
                // Invoke the callback with the fetched data
                callback(moviesList)
            },

            // RUN CODE ON FAILURE
            createErrorListener()
        ) {

        // Set AUTH Headers
        override fun getHeaders(): MutableMap<String, String> {
            return createHeaders(apiKey) // Reusing the headers function
        }
    }

    queue.add(request)
    return moviesList
}


fun getRecommendedMovies(
    queue: RequestQueue,
    apiKey: String,
    movieID: Int,
    callback: (ArrayList<MovieModel>) -> Unit // Callback parameter
): ArrayList<MovieModel> {

    val url = "https://api.themoviedb.org/3/movie/$movieID/recommendations"
    Log.e("MOVIEMATE", url)
    val moviesList = ArrayList<MovieModel>()

    val request = object :
        JsonObjectRequest(
            Method.GET, url, null,

            // RUN CODE ON SUCCESS
            Response.Listener { response ->
                val resultsArray = response.getJSONArray("results")
                for (i in 0 until resultsArray.length()) {
                    val movieObject = resultsArray.getJSONObject(i)
                    val movie = getParsedMovieModel(movieObject)
                    Log.e("MOVIEMATE", movie.title)
                    moviesList.add(movie)
                }
                // Invoke the callback with the fetched data
                callback(moviesList)
            },

            // RUN CODE ON FAILURE
            createErrorListener()
        ) {

        // Set AUTH Headers
        override fun getHeaders(): MutableMap<String, String> {
            return createHeaders(apiKey) // Reusing the headers function
        }
    }

    queue.add(request)
    return moviesList
}


// MOVIE DETAILS
fun getMovieDetails(
    queue: RequestQueue,
    apiKey: String,
    movieID: Int,
    callbackFunc: (MovieModel) -> Unit
) {
    val url = "https://api.themoviedb.org/3/movie/$movieID"
    lateinit var movieDetails: MovieModel

    val request = object :
        JsonObjectRequest(
            Method.GET, url, null,

            // RUN CODE ON SUCCESS
            Response.Listener { response ->
                movieDetails = getParsedMovieModel(response)
                movieDetails.genres = getGenreNames(response)
                callbackFunc(movieDetails)
            },

            // RUN CODE ON FAILURE
            createErrorListener()
        ) {

        // Set AUTH Headers
        override fun getHeaders(): MutableMap<String, String> {
            return createHeaders(apiKey) // Reusing the headers function
        }
    }

    queue.add(request)
}