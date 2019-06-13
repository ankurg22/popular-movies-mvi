package com.ankur.popularmovies

import com.ankur.popularmovies._http.MoviesApi
import com.ankur.popularmovies._repository.Error
import com.ankur.popularmovies._repository.ErrorType
import com.ankur.popularmovies._repository.FetchEvent
import com.ankur.popularmovies._repository.PopularMoviesRepositoryImpl
import io.reactivex.Observable
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.net.SocketTimeoutException

class PopularMoviesRepositoryTests {
  @Test fun fetchingPopularMoviesFailsDueToConnectionError() {
    // Setup
    val movieApi = mock(MoviesApi::class.java)
    val repository = PopularMoviesRepositoryImpl(movieApi)
    `when`(movieApi.getTopRatedMovies())
        .thenReturn(Observable.error(SocketTimeoutException()))

    // Act
    val observer = repository
      .fetchMovies()
      .test()

    // Assert
    observer
      .assertNoErrors()
      .assertValues(
        FetchEvent(FetchAction.IN_PROGRESS, emptyList()),
        FetchEvent(FetchAction.FETCH_FAILED, emptyList(), Error(ErrorType.CONNECTION))
      )
      .assertTerminated()
  }
}
