package com.ankur.popularmovies

import io.reactivex.Observable

interface MoviesApi {
  fun getMovies(): Observable<List<Movie>>
}
