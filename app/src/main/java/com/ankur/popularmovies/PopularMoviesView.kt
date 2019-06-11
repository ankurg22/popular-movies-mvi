package com.ankur.popularmovies

interface PopularMoviesView {

    fun showResults(movies: List<Movie>)

    fun showProgress()

    fun hideProgress()

    fun showError(error: Error)

    fun render(state: PopularMoviesState) {
        when (state.fetchAction) {
            FetchAction.IN_PROGRESS -> {
                showProgress()
            }

            FetchAction.FETCH_SUCCESSFUL -> {
                if (state.searchedMovies.isNotEmpty() and state.movies.isNotEmpty()) {
                    showResults(state.searchedMovies)
                } else {
                    hideProgress()
                    showResults(state.movies)
                }
            }

            FetchAction.FETCH_FAILED -> {
                hideProgress()
                state.error?.let { showError(it) }
            }
        }
    }
}