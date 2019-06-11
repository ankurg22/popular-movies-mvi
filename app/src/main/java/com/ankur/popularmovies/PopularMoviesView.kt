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
                hideProgress()
                showResults(state.movies)
            }
        }
    }
}