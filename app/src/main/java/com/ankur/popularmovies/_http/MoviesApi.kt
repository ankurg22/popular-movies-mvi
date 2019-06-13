package com.ankur.popularmovies._http

import com.ankur.popularmovies.BuildConfig
import io.reactivex.Observable
import retrofit2.http.GET

interface MoviesApi {
  @GET("movie/top_rated/?api_key=${BuildConfig.API_KEY}")
  fun getTopRatedMovies(): Observable<MoviesResponse>
}
