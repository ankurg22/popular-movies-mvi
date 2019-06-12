package com.ankur.popularmovies

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.net.SocketTimeoutException

class PopularMoviesModelTests {
  @Test fun `user sees an error when fetching movies fails`() {
    // Setup
    val lifecycle = PublishSubject.create<MviLifecycle>()
    val moviesApi = mock(MoviesApi::class.java)

    val searchIntention = PublishSubject.create<String>()
    val intentions = PopularMoviesIntentions(searchIntention)

    `when`(moviesApi.getMovies())
        .thenReturn(Observable.error(SocketTimeoutException()))

    val states = PublishSubject.create<PopularMoviesState>()

    val observer = PopularMoviesModel
        .bind(lifecycle, moviesApi, intentions, states)
        .test()

    // Act
    lifecycle.onNext(MviLifecycle.CREATED)

    // Assert
    val error = Error(ErrorType.CONNECTION)

    observer.assertNoErrors()
    observer.assertValues(
        PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null),
        PopularMoviesState(FetchAction.FETCH_FAILED, emptyList(), emptyList(), error)
    )
  }

  @Test fun `user sees a list of movies when fetching movies succeeds`() {
    // Setup
    val lifecycle = PublishSubject.create<MviLifecycle>()
    val moviesApi = mock(MoviesApi::class.java)
    val movies = listOf(
        Movie(1, "abc", "cde"),
        Movie(2, "abc", "cde")
    )
    val searchIntention = PublishSubject.create<String>()
    val intentions = PopularMoviesIntentions(searchIntention)

    `when`(moviesApi.getMovies())
        .thenReturn(Observable.just(movies))

    val states = PublishSubject.create<PopularMoviesState>()

    val observer = PopularMoviesModel
        .bind(lifecycle, moviesApi, intentions, states)
        .test()

    // Act
    lifecycle.onNext(MviLifecycle.CREATED)

    // Assert
    observer.assertNoErrors()
    observer.assertValues(
        PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null),
        PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null)
    )
  }

  @Test fun `user search the movie by name and search succeeds`(){
    // Setup
    val lifecycle = PublishSubject.create<MviLifecycle>()
    val moviesApi = mock(MoviesApi::class.java)
    val movies = listOf(
        Movie(1, "Race 3", "cde"),
        Movie(2, "abc", "cde")
    )
    val filteredMovies = listOf(
        Movie(1, "Race 3", "cde")
    )

    val searchIntention = PublishSubject.create<String>()
    val intentions = PopularMoviesIntentions(searchIntention)

    `when`(moviesApi.getMovies())
        .thenReturn(Observable.just(movies))

    val states = PublishSubject.create<PopularMoviesState>()

    val observer = PopularMoviesModel
        .bind(lifecycle, moviesApi, intentions, states)
        .doOnNext { states.onNext(it) }
        .test()

    // Act
    lifecycle.onNext(MviLifecycle.CREATED)
    searchIntention.onNext("Race 3")

    // Assert
    observer.assertNoErrors()
        .assertValues(
            PopularMoviesState(FetchAction.IN_PROGRESS, emptyList(), emptyList(), null),
            PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, emptyList(), null),
            PopularMoviesState(FetchAction.FETCH_SUCCESSFUL, movies, filteredMovies, null)
        )
  }
}