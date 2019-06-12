package com.ankur.popularmovies

import io.reactivex.Observable

class PopularMoviesIntentions(
  val searchIntention: Observable<String>,
  val retryIntention: Observable<Unit>
) {
  fun searchIntention() = searchIntention

  fun retryIntention() = retryIntention
}
