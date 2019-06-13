package com.ankur.popularmovies._repository

import com.ankur.popularmovies._http.Movie
import io.reactivex.Observable

interface PopularMoviesRepository {
  fun fetchMovies(): Observable<FetchEvent<List<Movie>>>
}
