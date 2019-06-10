package com.ankur.popularmovies

import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import java.io.IOException

object PopularMoviesModel {
  fun bind(
      lifecycle: Observable<MviLifecycle>,
      moviesApi: MoviesApi,
      intentions: PopularMoviesIntentions,
      states: Observable<PopularMoviesState>
  ): Observable<PopularMoviesState> {
    val lifecycleState = lifecycle
        .filter { it == MviLifecycle.CREATED }
        .switchMap {
          val inProgressState = Observable.just(PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null))

          val networkStates = moviesApi
              .getMovies()
              .map { movies -> PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null) }
              .onErrorReturn { throwable -> PopularMoviesState(FetchAction.FETCH_FAILED, emptyList(), emptyList(), parseNetworkError(throwable)) }

          return@switchMap Observable.concat(
              inProgressState,
              networkStates
          )
        }

    val searchState = intentions
        .searchIntention()
        .withLatestFrom(states)
        .map {(query, state) ->
          val allMovies = state.movies
          val filteredList = allMovies
              .filter { it.title.startsWith(query, true) }
              .toList()
          state.copy(searchedMovies = filteredList)
        }

    return Observable.merge(
        lifecycleState,
        searchState
    )
  }

  fun parseNetworkError(throwable: Throwable): Error {
    return if (throwable is IOException) {
      Error(ErrorType.CONNECTION)
    } else {
      Error(ErrorType.UNKNOWN)
    }
  }
}
