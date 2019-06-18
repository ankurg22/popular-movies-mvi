package com.ankur.popularmovies

import com.ankur.popularmovies.FetchAction.IN_PROGRESS
import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._repository.Error

interface PopularMoviesView {
  fun render(state: PopularMoviesState) {
    when (state.fetchAction) {
      IN_PROGRESS -> {
        if (state.movies.isEmpty()) {
          showProgress(true)
        } else {
          showResults(state.movies)
          showSilentProgress(true)
        }
      }
    }
  }

  // View access
  fun showResults(movies: List<Movie>)
  fun showProgress(show: Boolean)
  fun showSilentProgress(show: Boolean)
  fun showError(error: Error)
}
