package com.ankur.popularmovies

import com.ankur.popularmovies._mvi.MviLifecycle
import com.ankur.popularmovies._repository.PopularMoviesRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom

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
        moviesRepository
          .fetchMovies()
          .map { fetchEvent ->
            PopularMoviesState(
              fetchEvent.fetchAction,
              fetchEvent.result.orEmpty(),
              emptyList(),
              fetchEvent.error
            )
          }
      }

    val retryState = intentions
      .retryClicks()
      .withLatestFrom(states)
      .switchMap { (_, state) ->
        moviesRepository
          .fetchMovies()
          .map { fetchEvent ->
            state.copy(
              fetchAction = fetchEvent.fetchAction,
              movies = fetchEvent.result.orEmpty(),
              error = null
            )
          }
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
}
