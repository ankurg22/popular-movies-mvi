package com.ankur.popularmovies

import com.ankur.popularmovies.FetchAction.FETCH_FAILED
import com.ankur.popularmovies.FetchAction.FETCH_SUCCESSFUL
import com.ankur.popularmovies.FetchAction.IN_PROGRESS
import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._repository.Error

interface PopularMoviesView {
  fun render(state: PopularMoviesState) {
    when (state.fetchAction) {
      IN_PROGRESS -> renderFetchInProgress(state)

      FETCH_FAILED -> renderFetchFailed(state)

      FETCH_SUCCESSFUL -> renderFetchSuccessful(state)
    }
  }

  fun renderFetchSuccessful(state: PopularMoviesState) {
    val hasMovies = state.searchedMovies.isNotEmpty() and state.movies.isNotEmpty()
    if (hasMovies) {
      showResults(state.searchedMovies)
    } else {
      showProgress(false)
      showSilentProgress(false)
      showResults(state.movies)
    }
  }

  fun renderFetchFailed(state: PopularMoviesState) {
    showProgress(false)
    showSilentProgress(false)
    state.error?.let { showError(it) }
  }

  fun renderFetchInProgress(state: PopularMoviesState) {
    if (state.movies.isEmpty()) {
      showProgress(true)
    } else {
      showResults(state.movies)
      showSilentProgress(true)
    }
  }

  // View access
  fun showResults(movies: List<Movie>)
  fun showProgress(show: Boolean)
  fun showSilentProgress(show: Boolean)
  fun showError(error: Error)
}
