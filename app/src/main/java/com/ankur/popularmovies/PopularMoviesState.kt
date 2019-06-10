package com.ankur.popularmovies

data class PopularMoviesState(
    val fetchAction: FetchAction,
    val movies: List<Movie>,
    val searchedMovies: List<Movie>,
    val error: Error?
)

enum class FetchAction {
  IN_PROGRESS,

  FETCH_SUCCESSFUL,

  FETCH_FAILED
}
