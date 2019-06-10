package com.ankur.popularmovies

data class PopularMoviesState(
    val fetchAction: FetchAction,
    val error: Error?
)

enum class FetchAction {
  IN_PROGRESS,

  FETCH_FAILED
}

data class Error(
    val code: Int
)
