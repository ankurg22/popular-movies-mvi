package com.ankur.popularmovies

interface PopularMoviesView {

    fun showResults(movies: List<Movie>)

    fun showProgress()

    fun hideProgress()

    fun showError(error: Error)

    fun render(state: PopularMoviesState) {
        showProgress()
    }
}