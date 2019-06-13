package com.ankur.popularmovies._repository

import com.ankur.popularmovies.FetchAction
import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._http.MoviesApi
import io.reactivex.Observable
import java.io.IOException

class PopularMoviesRepositoryImpl(
    private val moviesApi: MoviesApi
) : PopularMoviesRepository {
    override fun fetchMovies(): Observable<FetchEvent<List<Movie>>> {
        val inProgressEvents = Observable.just(FetchEvent(FetchAction.IN_PROGRESS, emptyList<Movie>()))

        val networkEvents = moviesApi
            .getTopRatedMovies()
            .map { response ->
                FetchEvent(FetchAction.FETCH_SUCCESSFUL, response.movies)
            }
            .onErrorReturn { error ->
                FetchEvent(FetchAction.FETCH_FAILED, emptyList(), getError(error))
            }

        return Observable.concat(
            inProgressEvents,
            networkEvents
        )
    }

    private fun getError(throwable: Throwable): Error {
        return if (throwable is IOException) {
            Error(ErrorType.CONNECTION)
        } else {
            Error(ErrorType.UNKNOWN)
        }
    }
}
