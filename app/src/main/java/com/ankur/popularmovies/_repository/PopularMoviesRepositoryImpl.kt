package com.ankur.popularmovies._repository

import com.ankur.popularmovies.FetchAction
import com.ankur.popularmovies._db.PopularMoviesDatabase
import com.ankur.popularmovies._http.Movie
import com.ankur.popularmovies._http.MoviesApi
import io.reactivex.Observable
import java.io.IOException

class PopularMoviesRepositoryImpl(
  private val appDatabase: PopularMoviesDatabase,
  private val moviesApi: MoviesApi,
  private val schedulerProvider: SchedulerProvider
) : PopularMoviesRepository {
  private val moviesDao = appDatabase.movieDao()

  override fun fetchMovies(): Observable<FetchEvent<List<Movie>>> {
    val inProgressEvents = moviesDao
      .getAll()
      .map { FetchEvent(FetchAction.IN_PROGRESS, it) }
      .toObservable()

    val networkEvents = moviesApi
      .getTopRatedMovies()
      .map { FetchEvent(FetchAction.FETCH_SUCCESSFUL, it.movies) }
      .onErrorReturn { FetchEvent(FetchAction.FETCH_FAILED, emptyList(), getError(it)) }

    return Observable.concat(
      inProgressEvents,
      networkEvents
    )
      .subscribeOn(schedulerProvider.io())
  }

  private fun getError(throwable: Throwable): Error {
    return if (throwable is IOException) {
      Error(ErrorType.CONNECTION)
    } else {
      Error(ErrorType.UNKNOWN)
    }
  }
}
