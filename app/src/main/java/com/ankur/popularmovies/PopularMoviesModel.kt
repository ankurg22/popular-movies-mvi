package com.ankur.popularmovies

import io.reactivex.Observable
import java.io.IOException

object PopularMoviesModel {
  fun bind(
      lifecycle: Observable<MviLifecycle>,
      moviesApi: MoviesApi
  ): Observable<PopularMoviesState> {
    return lifecycle
        .filter { it == MviLifecycle.CREATED }
        .switchMap {
          val inProgressState = Observable.just(PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), null))

          val networkStates = moviesApi
              .getMovies()
              .map { movies -> PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, null) }
              .onErrorReturn { throwable -> PopularMoviesState(FetchAction.FETCH_FAILED, emptyList(), parseNetworkError(throwable)) }

          return@switchMap Observable.concat(
              inProgressState,
              networkStates
          )
        }
  }

  fun parseNetworkError(throwable: Throwable): Error {
    return if (throwable is IOException) {
      Error(ErrorType.CONNECTION)
    } else {
      Error(ErrorType.UNKNOWN)
    }
  }
}
