package com.ankur.popularmovies._repository

import com.ankur.popularmovies.FetchAction
import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._http.MoviesApi
import io.reactivex.Observable

class PopularMoviesRepositoryImpl(
    private val moviesApi: MoviesApi
) : PopularMoviesRepository {
    override fun fetchMovies(): Observable<FetchEvent<List<Movie>>> {
        return Observable.just(
            FetchEvent(FetchAction.IN_PROGRESS, emptyList()),
            FetchEvent(FetchAction.FETCH_FAILED, emptyList(), Error(ErrorType.CONNECTION))
        )
    }
}
