package com.ankur.popularmovies

import io.reactivex.Observable

class PopularMoviesIntentions(
  private val search: Observable<String>,
  private val retry: Observable<Unit>
) {
  fun search() = search

  fun retryClicks() = retry
}
