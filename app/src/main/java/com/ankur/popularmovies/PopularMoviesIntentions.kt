package com.ankur.popularmovies

import io.reactivex.subjects.PublishSubject

class PopularMoviesIntentions(val searchIntention: PublishSubject<String>) {
  fun searchIntention() = searchIntention
}
