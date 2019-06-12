package com.ankur.popularmovies

import com.ankur.popularmovies.FetchAction.FETCH_FAILED
import com.ankur.popularmovies.FetchAction.FETCH_SUCCESSFUL
import com.ankur.popularmovies.FetchAction.IN_PROGRESS

interface PopularMoviesView {
  fun render(state: PopularMoviesState) {
    when (state.fetchAction) {
      IN_PROGRESS -> showProgress(true)
      FETCH_SUCCESSFUL -> renderFetchSuccessful(state)
      FETCH_FAILED -> renderFetchFailed(state)
    }
  }

  fun renderFetchSuccessful(state: PopularMoviesState) {
    val hasMovies = state.searchedMovies.isNotEmpty() and state.movies.isNotEmpty()
    if (hasMovies) {
      showResults(state.searchedMovies)
    } else {
      showProgress(false)
      showResults(state.movies)
    }
  }

  fun renderFetchFailed(state: PopularMoviesState) {
    showProgress(false)
    state.error?.let { showError(it) }
  }

  // View access
  fun showResults(movies: List<Movie>)
  fun showProgress(show: Boolean)
  fun showError(error: Error)
}
