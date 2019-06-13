package com.ankur.popularmovies._repository

import com.ankur.popularmovies._http.Movie
import io.reactivex.Observable

class PopularMoviesRepositoryImpl: PopularMoviesRepository {
    override fun fetchMovies(): Observable<FetchEvent<List<Movie>>> {
        TODO("not implemented") // Not implemented
    }
}
