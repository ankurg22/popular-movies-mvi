package com.ankur.popularmovies

import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._repository.Error

open class SpyablePopularMoviesView : PopularMoviesView {
  override fun showProgress(show: Boolean) {}
  override fun showResults(movies: List<Movie>) {}
  override fun showError(error: Error) {}
}
