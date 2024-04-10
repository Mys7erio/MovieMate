package com.example.moviemate

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


private fun getParsedMovieModel(movieObject: JSONObject): MovieModel {
    return MovieModel(
        id = movieObject.getString("id"),
        title = movieObject.getString("title"),
        description = movieObject.getString("overview"),
        voteAverage = movieObject.getDouble("vote_average"),
        releaseDate = movieObject.getString("release_date"),
        posterPath = movieObject.getString("poster_path"),
        backdropPath = movieObject.getString("backdrop_path"),
        genres = (0 until movieObject.getJSONArray("genre_ids")
            .length()).map { index ->
            movieObject.getJSONArray("genre_ids").getInt(index).toString()
        }
    )
}

fun createErrorListener(): Response.ErrorListener {
    // Function to create the error listener
    return Response.ErrorListener { error ->
        if (error.networkResponse != null) {
            val errorMessage = String(error.networkResponse.data)
            Log.e("MOVIEMATE", "Volley Error: $errorMessage")
        } else {
            Log.e("MOVIEMATE", "Volley Error: ${error.message}")
        }
    }
}

private fun createHeaders(apiKey: String): MutableMap<String, String> {
    // Function to set headers
    val headers = HashMap<String, String>()
    headers["Authorization"] = "Bearer $apiKey"
    return headers
}


fun getTrending(
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


fun getRecommendations(
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