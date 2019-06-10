package com.ankur.popularmovies

import io.reactivex.Observable

object PopularMoviesModel {
  fun bind(
      lifecycle: Observable<MviLifecycle>,
      moviesApi: MoviesApi
  ): Observable<PopularMoviesState> {
    return Observable.just(
        PopularMoviesState(FetchAction.IN_PROGRESS, null),
        PopularMoviesState(FetchAction.FETCH_FAILED, Error(500))
    )
  }
}
