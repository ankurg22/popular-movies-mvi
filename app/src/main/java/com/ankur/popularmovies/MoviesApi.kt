package com.ankur.popularmovies

import io.reactivex.Observable
import retrofit2.http.GET

interface MoviesApi {
    @GET("movie/top_rated/?=$API_KEY")
    fun getTopRatedMovies(): Observable<MoviesResponse>
}
