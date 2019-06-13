package com.ankur.popularmovies

import io.reactivex.Observable

interface PopularMoviesRepository {
  fun fetchMovies(): Observable<FetchEvent<List<Movie>>>
}