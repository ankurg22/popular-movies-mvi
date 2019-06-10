package com.ankur.popularmovies

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.net.SocketTimeoutException

class PopularMoviesModelTests {
  @Test
  fun `user sees an error when fetching movies fails`() {
    // Setup
    val lifecycle = PublishSubject.create<MviLifecycle>()
    val moviesApi = mock(MoviesApi::class.java)

    `when`(moviesApi.getMovies())
        .thenReturn(Observable.error(SocketTimeoutException()))

    val observer = PopularMoviesModel
        .bind(lifecycle, moviesApi)
        .test()

    // Act
    lifecycle.onNext(MviLifecycle.CREATED)

    // Assert
    val error = Error(500)

    observer.assertNoErrors()
    observer.assertValues(
        PopularMoviesState(FetchAction.IN_PROGRESS, null),
        PopularMoviesState(FetchAction.FETCH_FAILED, error)
    )
  }
}