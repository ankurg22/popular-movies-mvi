package com.ankur.popularmovies

import io.reactivex.Observable

class PopularMoviesIntentions(
  private val searchQueryChanges: Observable<String>,
  private val retryClicks: Observable<Unit>
) {
  fun search() = searchQueryChanges

  fun retryClicks() = retryClicks
}
