package com.ankur.popularmovies._http

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id")
    val id: Int,

    @SerializedName("original_title")
    val title: String,

    @SerializedName("poster_path")
    val poster: String
)

data class MoviesResponse(
    @SerializedName("results")
    val movies: List<Movie>
)
