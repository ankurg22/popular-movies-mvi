package com.ankur.popularmovies

interface PopularMoviesView {
    fun showResults(movies: List<Movie>)
    fun showProgress(show:Boolean)
    fun showError(error: Error)
    fun render(state: PopularMoviesState) {
        when (state.fetchAction) {
            FetchAction.IN_PROGRESS -> {
                showProgress(true)
            }

            FetchAction.FETCH_SUCCESSFUL -> {
                if (state.searchedMovies.isNotEmpty() and state.movies.isNotEmpty()) {
                    showResults(state.searchedMovies)
                } else {
                    showProgress(false)
                    showResults(state.movies)
                }
            }

            FetchAction.FETCH_FAILED -> {
                showProgress(false)
                state.error?.let { showError(it) }
            }
        }
    }
}
