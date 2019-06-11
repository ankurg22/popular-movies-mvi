package com.ankur.popularmovies

open class SpyablePopularMoviesView : PopularMoviesView {
    override fun showProgress() {}

    override fun hideProgress() {}

    override fun showResults(movies: List<Movie>) {}


    override fun showError(error: Error) {}
}