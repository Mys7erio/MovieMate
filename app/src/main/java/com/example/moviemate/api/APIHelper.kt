package com.example.moviemate.api

import android.graphics.Bitmap
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.example.moviemate.MovieModel
import org.json.JSONObject

fun getParsedMovieModel(movieObject: JSONObject): MovieModel {
    return MovieModel(
        id = movieObject.getInt("id"),
        title = movieObject.getString("title"),
        overview = movieObject.getString("overview"),
        voteAverage = movieObject.getDouble("vote_average"),
        releaseDate = movieObject.getString("release_date"),
        posterPath = movieObject.getString("poster_path"),
        backdropPath = movieObject.getString("backdrop_path"),
        // Not adding genres list by default
    )
}

fun getGenreNames(movieObject: JSONObject): MutableList<String> {
    val genresNames = mutableListOf<String>()
    val genresArray = movieObject.getJSONArray("genres")
    for (i in 0 until genresArray.length()) {
        val genreObject = genresArray.getJSONObject(i)
        val genreName = genreObject.getString("name")
        genresNames.add(genreName)
    }

    return genresNames
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


fun createHeaders(apiKey: String): MutableMap<String, String> {
    // Function to set headers
    val headers = HashMap<String, String>()
    headers["Authorization"] = "Bearer $apiKey"
    return headers
}



fun setImage(
    requestQueue: RequestQueue,
    imageUrl: String,
    callbackFunc: (image: Bitmap) -> Unit
) {

    val request = ImageRequest(
        imageUrl,
        { imageResponse ->
            callbackFunc(imageResponse)
        }, 0, 0, null, null,

        createErrorListener() // Handle Errors
    )
    requestQueue.add(request)
    Log.e("MOVIEMATE", "FETCHED IMAGE")

}