package com.ankur.popularmovies._repository

import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._http.MoviesApi
import io.reactivex.Observable

class PopularMoviesRepositoryImpl(
    private val kiteTabApi: MoviesApi
): PopularMoviesRepository {
    override fun fetchMovies(): Observable<FetchEvent<List<Movie>>> {
        TODO("not implemented") // Not implemented
    }
}
