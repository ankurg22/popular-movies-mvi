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
        val inProgressState =
          Observable.just(PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null))

        val networkStates = moviesApi
          .getTopRatedMovies()
          .map { it.movies }
          .map { movies -> PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null) }
          .onErrorReturn { throwable ->
            PopularMoviesState(
              FetchAction.FETCH_FAILED,
              emptyList(),
              emptyList(),
              parseNetworkError(throwable)
            )
          }

        return@switchMap Observable.concat(
          inProgressState,
          networkStates
        )
      }

    val retryState = intentions
      .retryIntention()
      .withLatestFrom(states)
      .switchMap { (_, state) ->
        val inProgressState =
          Observable.just(state.copy(fetchAction = FetchAction.IN_PROGRESS, movies = emptyList(), error = null))

        val networkStates = moviesApi
          .getTopRatedMovies()
          .map { it.movies }
          .map { movies -> state.copy(fetchAction = FetchAction.FETCH_SUCCESSFUL, movies = movies, error = null) }
          .onErrorReturn { throwable ->
            state.copy(fetchAction = FetchAction.FETCH_FAILED, error = parseNetworkError(throwable))
          }

        return@switchMap Observable.concat(
          inProgressState,
          networkStates
        )
      }

    val restoredState = lifecycle
      .filter { it == MviLifecycle.RESTORED }
      .withLatestFrom(states)
      .map { (_, state) -> state }

    val searchState = intentions
      .searchIntention()
      .withLatestFrom(states)
      .map { (query, state) ->
        val allMovies = state.movies
        val filteredList = allMovies
          .filter { it.title.startsWith(query, true) }
          .toList()
        state.copy(searchedMovies = filteredList)
      }

    return Observable.merge(
      lifecycleState,
      searchState,
      retryState,
      restoredState
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
