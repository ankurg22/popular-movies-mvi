package com.ankur.popularmovies

import com.ankur.popularmovies._mvi.MviLifecycle
import com.ankur.popularmovies._repository.Error
import com.ankur.popularmovies._repository.ErrorType
import com.ankur.popularmovies._repository.PopularMoviesRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import java.io.IOException

object PopularMoviesModel {
  fun bind(
      lifecycle: Observable<MviLifecycle>,
      moviesRepository: PopularMoviesRepository,
      intentions: PopularMoviesIntentions,
      states: Observable<PopularMoviesState>
  ): Observable<PopularMoviesState> {
    val lifecycleState = lifecycle
      .filter { it == MviLifecycle.CREATED }
      .switchMap {
        val inProgressState =
          Observable.just(PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null))

        val networkStates = moviesRepository
          .fetchMovies()
          .map { it.result }
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
      .retryClicks()
      .withLatestFrom(states)
      .switchMap { (_, state) ->
        val inProgressState =
          Observable.just(state.copy(fetchAction = FetchAction.IN_PROGRESS, movies = emptyList(), error = null))

        val networkStates = moviesRepository
          .fetchMovies()
          .map { it.result }
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
      .search()
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
